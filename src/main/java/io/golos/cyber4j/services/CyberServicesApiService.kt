package io.golos.cyber4j.services

import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.KeyStorage
import io.golos.cyber4j.OnKeysAddedListener
import io.golos.cyber4j.model.*
import io.golos.cyber4j.services.model.*
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.StringSigner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

private enum class ServicesGateMethods {
    GET_FEED, GET_POST, GET_COMMENTS, GET_USER_METADATA, GET_SECRET, AUTH;

    override fun toString(): String {
        return when (this) {
            GET_FEED -> "content.getFeed"
            GET_POST -> "content.getPost"
            GET_COMMENTS -> "content.getComments"
            GET_USER_METADATA -> "content.getProfile"
            GET_SECRET -> "auth.generateSecret"
            AUTH -> "auth.authorize"
        }
    }
}


internal class CyberServicesApiService(private val config: Cyber4JConfig,
                                       private val keyStore: KeyStorage,
                                       private val apiClient: ApiClient = CyberServicesWebSocketClient(config)) :
        ApiService, AuthRequestListener, OnKeysAddedListener {

    private val authExecutor = Executors.newSingleThreadExecutor()
    private var lock: CountDownLatch? = null
    private var authListeners = ArrayList<AuthListener>()
    private val isAuthRunning = AtomicBoolean(false)

    init {
        apiClient.setAuthRequestListener(this)
        keyStore.addOnKeyChangedListener(this)
    }

    override fun addOnAuthListener(listener: AuthListener) {
        authListeners.add(listener)

    }

    override fun onActiveKeysAdded(newUser: CyberName,
                                   activeKey: String,
                                   oldUser: CyberName?) {
        if (oldUser != null && oldUser != newUser) {
            apiClient.unAuth()
        } else if (oldUser == null) {
            authIfPossible()
        }
    }

    override fun onAuthRequest(secret: String) {
        authIfPossible(secret)
    }


    private fun authIfPossible(presetSecret: String? = null) {
        if (isAuthRunning.get()) return
        if (keyStore.isActiveAccountSet()) {
            lock()
            isAuthRunning.set(true)
            authExecutor.execute {
                if (!keyStore.isActiveAccountSet()) {
                    releaseLock()
                    return@execute
                }
                try {
                    val activeAccount = keyStore.getActiveAccount()
                    val activeAccountActiveKey = keyStore.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second

                    if (activeAccountActiveKey == null) {
                        releaseLock()
                        return@execute
                    }

                    val secret = presetSecret
                            ?: {
                                val resp = apiClient.send(ServicesGateMethods.GET_SECRET.toString(),
                                        GetSecretRequest(), AuthSecret::class.java) as Either.Success
                                resp.value.secret
                            }()


                    apiClient.send(ServicesGateMethods.AUTH.toString(),
                            ServicesAuthRequest(activeAccount.name,
                                    StringSigner.signString(secret, activeAccountActiveKey),
                                    secret), Any::class.java) as Either.Success


                    for (authListener in authListeners) {
                        authListener.onAuthSuccess(activeAccount)
                    }
                    releaseLock()
                    isAuthRunning.set(false)
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    releaseLock()
                    isAuthRunning.set(false)
                    System.err.println("active account not set")
                    authListeners.forEach { it.onFail(e) }
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                    releaseLock()
                    isAuthRunning.set(false)
                    System.err.println("response error")
                    authListeners.forEach { it.onFail(e) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    releaseLock()
                    isAuthRunning.set(false)
                    System.err.println("unknown error")
                    authListeners.forEach { it.onFail(e) }
                }
            }
        } else releaseLock()
    }

    override fun getDiscussions(feedType: PostsFeedType,
                                sort: DiscussionTimeSort,
                                sequenceKey: String?,
                                limit: Int,
                                userId: String?,
                                communityId: String?): Either<DiscussionsResult, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(ServicesGateMethods.GET_FEED.toString(),
                DiscussionsRequests(feedType.toString(),
                        sort.toString(),
                        sequenceKey,
                        limit,
                        userId,
                        communityId), DiscussionsResult::class.java)
    }

    override fun getDiscussion(userId: String, permlink: String, refBlockNum: Long): Either<CyberDiscussion, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(ServicesGateMethods.GET_POST.toString(), DiscussionRequests(userId, permlink, refBlockNum), CyberDiscussion::class.java)
    }

    override fun getComments(sort: DiscussionTimeSort, sequenceKey: String?,
                             limit: Int, origin: CommentsOrigin, userId: String?,
                             permlink: String?, refBlockNum: Int?): Either<DiscussionsResult, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(ServicesGateMethods.GET_COMMENTS.toString(),
                CommentsRequest(sort.toString(), sequenceKey, limit, origin.toString(), userId, permlink, refBlockNum), DiscussionsResult::class.java)
    }

    override fun getUserMetadata(userId: String): Either<UserMetadata, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(ServicesGateMethods.GET_USER_METADATA.toString(),
                UserMetaDataRequest(userId), UserMetadata::class.java)
    }

    @Synchronized
    private fun lockIfNeeded() {
        if (lock != null && lock!!.count > 0) lock!!.await(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
    }

    private fun releaseLock() {
        lock?.countDown()
        lock = null
    }

    private fun lock() {
        releaseLock()
        lock = CountDownLatch(1)
    }
}