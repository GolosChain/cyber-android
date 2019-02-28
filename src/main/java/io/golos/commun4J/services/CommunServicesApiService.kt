package io.golos.commun4J.services

import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.CommunKeyStorage
import io.golos.commun4J.OnKeysAddedListener
import io.golos.commun4J.model.*
import io.golos.commun4J.services.model.*
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.StringSigner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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


internal class CommunServicesApiService(private val config: Commun4JConfig,
                                        private val keyStore: CommunKeyStorage,
                                        private val apiClient: ApiClient = CommunServicesWebSocketClient(config)) :
        ApiService, AuthRequestListener, OnKeysAddedListener {

    private val authExecutor = Executors.newSingleThreadExecutor()
    private var lock: CountDownLatch? = null
    private var authListeners = ArrayList<AuthListener>()

    init {
        apiClient.setAuthRequestListener(this)
        keyStore.addOnKeyChangedListener(this)
    }

    override fun addOnAuthListener(listener: AuthListener) {
        authListeners.add(listener)

    }

    override fun onActiveKeysAdded(newUser: CommunName,
                                   activeKey: String,
                                   oldUser: CommunName?) {
        lock()
        if (oldUser != null && oldUser != newUser) {
            apiClient.unAuth()
        } else if (oldUser == null) {
            authIfPossible()
        }
        releaseLock()
    }

    override fun onAuthRequest(secret: String) {
        authIfPossible(secret)
    }


    @Synchronized
    private fun authIfPossible(presetSecret: String? = null) {
        if (keyStore.isActiveAccountSet()) {
            lock()
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
                            CommunAuthRequest(activeAccount.name,
                                    StringSigner.signString(secret, activeAccountActiveKey),
                                    secret), Any::class.java) as Either.Success


                    for (authListener in authListeners) {
                        authListener.onAuthSuccess(activeAccount)
                    }
                    releaseLock()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    releaseLock()
                    System.err.println("active account not set")
                    authListeners.forEach { it.onFail(e) }
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                    releaseLock()
                    System.err.println("response error")
                    authListeners.forEach { it.onFail(e) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    releaseLock()
                    System.err.println("unknown error")
                    authListeners.forEach { it.onFail(e) }
                }
            }
        }
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

    override fun getDiscussion(userId: String, permlink: String, refBlockNum: Int): Either<CommunDiscussion, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(ServicesGateMethods.GET_POST.toString(), DiscussionRequests(userId, permlink, refBlockNum), CommunDiscussion::class.java)
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