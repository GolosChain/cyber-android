package io.golos.cyber4j.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.KeyStorage
import io.golos.cyber4j.OnKeysAddedListener
import io.golos.cyber4j.model.*
import io.golos.cyber4j.services.model.*
import io.golos.cyber4j.utils.*
import java.math.BigInteger
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

private enum class ServicesGateMethods {
    GET_FEED, GET_POST, GET_COMMENT, GET_COMMENTS, GET_USER_METADATA, GET_SECRET, AUTH, GET_EMBED,
    GET_REGISTRATION_STATE, REG_FIRST_STEP, REG_VERIFY_PHONE, REG_SET_USER_NAME, REG_WRITE_TO_BLOCKCHAIN,
    REG_RESEND_SMS, WAIT_BLOCK, PUSH_SUBSCRIBE, PUSH_UNSUBSCRIBE, GET_NOTIFS_HISTORY, MARK_VIEWED,
    GET_UNREAD_COUNT, MARK_VIEWED_ALL, SET_SETTINGS, GET_SETTINGS;

    override fun toString(): String {
        return when (this) {
            GET_FEED -> "content.getFeed"
            GET_POST -> "content.getPost"
            GET_COMMENT -> "content.getComment"
            GET_COMMENTS -> "content.getComments"
            WAIT_BLOCK -> "content.waitForBlock"
            GET_USER_METADATA -> "content.getProfile"
            GET_SECRET -> "auth.generateSecret"
            GET_EMBED -> "frame.getEmbed"
            AUTH -> "auth.authorize"
            GET_REGISTRATION_STATE -> "registration.getState"
            REG_FIRST_STEP -> "registration.firstStep"
            REG_VERIFY_PHONE -> "registration.verify"
            REG_SET_USER_NAME -> "registration.setUsername"
            REG_WRITE_TO_BLOCKCHAIN -> "registration.toBlockChain"
            REG_RESEND_SMS -> "registration.resendSmsCode"
            PUSH_SUBSCRIBE -> "push.notifyOn"
            PUSH_UNSUBSCRIBE -> "push.notifyOff"
            GET_NOTIFS_HISTORY -> "push.history"
            MARK_VIEWED -> "notify.markAsViewed"
            GET_UNREAD_COUNT -> "push.historyFresh"
            MARK_VIEWED_ALL -> "notify.markAllAsViewed"
            SET_SETTINGS -> "options.set"
            GET_SETTINGS -> "options.get"
        }
    }
}


internal class CyberServicesApiService(
        private val config: Cyber4JConfig,
        private val keyStore: KeyStorage,
        private val apiClient: ApiClient = CyberServicesWebSocketClient(
                config,
                moshi =
                Moshi.Builder()
                        .add(Date::class.java, Rfc3339DateJsonAdapter())
                        .add(BigInteger::class.java, BigIntegerAdapter())
                        .add(CyberName::class.java, CyberNameAdapter())
                        .add(UserRegistrationState::class.java, UserRegistrationStateAdapter())
                        .add(RegistrationStrategy::class.java, UserRegistrationStrategyAdapter())
                        .add(ContentRow::class.java, ContentRowAdapter())
                        .add(EventType::class.java, EventTypeAdapter())
                        .add(CyberNameAdapter())
                        .add(ServiceSettingsLanguage::class.java, ServiceSettingsLanguageAdapter())
                        .add(EventsAdapter())
                        .build()
        )
) :
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

    override fun onActiveKeysAdded(
            newUser: CyberName,
            activeKey: String,
            oldUser: CyberName?
    ) {
        if (!config.performAutoAuthOnActiveUserSet) return

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
                    val activeAccountActiveKey =
                            keyStore.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second

                    if (activeAccountActiveKey == null) {
                        releaseLock()
                        return@execute
                    }


                    val secret = presetSecret
                            ?: {
                                val resp = apiClient.send(
                                        ServicesGateMethods.GET_SECRET.toString(),
                                        GetSecretRequest(), AuthSecret::class.java
                                ) as Either.Success
                                resp.value.secret
                            }()


                    apiClient.send(
                            ServicesGateMethods.AUTH.toString(),
                            ServicesAuthRequest(
                                    activeAccount.name,
                                    StringSigner.signString(secret, activeAccountActiveKey),
                                    secret
                            ), Any::class.java
                    ) as Either.Success


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

    override fun getDiscussions(
            feedType: PostsFeedType,
            sort: DiscussionTimeSort,
            parsingType: ContentParsingType,
            sequenceKey: String?,
            limit: Int,
            userId: String?,
            communityId: String?
    ): Either<DiscussionsResult, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(
                ServicesGateMethods.GET_FEED.toString(),
                DiscussionsRequests(
                        feedType.toString(),
                        sort.toString(),
                        sequenceKey,
                        limit,
                        userId,
                        communityId,
                        parsingType.asContentType()
                ), DiscussionsResult::class.java
        )
    }

    override fun getPost(
            userId: String,
            permlink: String,
            refBlockNum: Long,
            parsingType: ContentParsingType
    ): Either<CyberDiscussion, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(
                ServicesGateMethods.GET_POST.toString(),
                DiscussionRequests(userId, permlink, refBlockNum, parsingType.asContentType()),
                CyberDiscussion::class.java
        )
    }

    override fun waitBlock(blockNum: Long): Either<ResultOk, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.WAIT_BLOCK.toString(),
                WaitForBlockRequest(blockNum),
                ResultOk::class.java
        )
    }

    override fun getComment(
            userId: String,
            permlink: String,
            refBlockNum: Long,
            parsingType: ContentParsingType
    ): Either<CyberDiscussion, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(
                ServicesGateMethods.GET_COMMENT.toString(), DiscussionRequests(
                userId,
                permlink,
                refBlockNum,
                parsingType.asContentType()
        ), CyberDiscussion::class.java
        )
    }

    override fun getComments(
            sort: DiscussionTimeSort, sequenceKey: String?,
            limit: Int, origin: CommentsOrigin,
            parsingType: ContentParsingType,
            userId: String?,
            permlink: String?, refBlockNum: Long?
    ): Either<DiscussionsResult, ApiResponseError> {

        lockIfNeeded()
        return apiClient.send(
                ServicesGateMethods.GET_COMMENTS.toString(),
                CommentsRequest(
                        sort.toString(),
                        sequenceKey, limit,
                        parsingType.asContentType(),
                        origin.toString(),
                        userId,
                        permlink,
                        refBlockNum
                ), DiscussionsResult::class.java
        )
    }

    override fun getIframelyEmbed(forLink: String): Either<IFramelyEmbedResult, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.GET_EMBED.toString(),
                EmbedRequest(EmbedService.IFRAMELY.toString(), forLink), IFramelyEmbedResult::class.java
        )
    }

    override fun getOEmdedEmbed(forLink: String): Either<OEmbedResult, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.GET_EMBED.toString(),
                EmbedRequest(EmbedService.OEMBED.toString(), forLink), OEmbedResult::class.java
        )
    }

    override fun getUserMetadata(userId: String): Either<UserMetadata, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.GET_USER_METADATA.toString(),
                UserMetaDataRequest(userId), UserMetadata::class.java
        )
    }

    override fun getRegistrationStateOf(
            userId: String?,
            phone: String?
    ): Either<UserRegistrationStateResult, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.GET_REGISTRATION_STATE.toString(),
                RegistrationStateRequest(userId, phone), UserRegistrationStateResult::class.java
        )
    }

    override fun firstUserRegistrationStep(
            captcha: String?,
            phone: String,
            testingPass: String?
    ): Either<FirstRegistrationStepResult, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.REG_FIRST_STEP.toString(),
                FirstRegistrationStepRequest(captcha, phone, testingPass), FirstRegistrationStepResult::class.java
        )
    }

    override fun verifyPhoneForUserRegistration(phone: String, code: Int): Either<ResultOk, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.REG_VERIFY_PHONE.toString(),
                VerifyPhoneRequest(phone, code), ResultOk::class.java
        )
    }

    override fun setVerifiedUserName(user: String, phone: String): Either<ResultOk, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.REG_SET_USER_NAME.toString(),
                RegistrationStateRequest(user, phone), ResultOk::class.java
        )
    }

    override fun writeUserToBlockchain(
            userName: String,
            owner: String,
            active: String,
            posting: String,
            memo: String
    ): Either<ResultOk, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.REG_WRITE_TO_BLOCKCHAIN.toString(),
                WriteUserToBlockchainRequest(userName, owner, active, posting, memo), ResultOk::class.java
        )
    }

    override fun resendSmsCode(name: String?, phone: String?): Either<ResultOk, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.REG_RESEND_SMS.toString(),
                ResendUserSmsRequest(name, phone), ResultOk::class.java
        )
    }

    override fun subscribeOnMobilePushNotifications(deviceId: String, fcmToken: String): Either<ResultOk, ApiResponseError> {
        lockIfNeeded()
        val request = PushSubscibeRequest(fcmToken, deviceId)
        return apiClient.send(
                ServicesGateMethods.PUSH_SUBSCRIBE.toString(),
                request, ResultOk::class.java
        )
    }

    override fun unSubscribeOnNotifications(deviceId: String, fcmToken: String): Either<ResultOk, ApiResponseError> {
        lockIfNeeded()
        val request = PushSubscibeRequest(fcmToken, deviceId)
        return apiClient.send(
                ServicesGateMethods.PUSH_UNSUBSCRIBE.toString(),
                request, ResultOk::class.java
        )
    }

    override fun setNotificationSettings(deviceId: String,
                                         newBasicSettings: Any?,
                                         newWebNotifySettings: WebShowSettings?,
                                         newMobilePushSettings: MobileShowSettings?): Either<ResultOk, ApiResponseError> {
        lockIfNeeded()
        val request = UserSettings(deviceId, newBasicSettings, newWebNotifySettings, newMobilePushSettings)

        return apiClient.send(
                ServicesGateMethods.SET_SETTINGS.toString(),
                request, ResultOk::class.java
        )
    }

    override fun getNotificationSettings(deviceId: String): Either<UserSettings, ApiResponseError> {
        lockIfNeeded()
        val request = ServicesSettingsRequest(deviceId)

        return apiClient.send(
                ServicesGateMethods.GET_SETTINGS.toString(),
                request, UserSettings::class.java
        )
    }

    override fun getEvents(userProfile: String,
                           afterId: String?,
                           limit: Int?,
                           markAsViewed: Boolean?,
                           freshOnly: Boolean?,
                           types: List<EventType>): Either<EventsData, ApiResponseError> {
        lockIfNeeded()
        val request = EventsRequest(userProfile, afterId, limit, types, markAsViewed, freshOnly)

        return apiClient.send(
                ServicesGateMethods.GET_NOTIFS_HISTORY.toString(),
                request, EventsData::class.java
        )
    }

    override fun markEventsAsRead(ids: List<String>): Either<ResultOk, ApiResponseError> {
        lockIfNeeded()
        val request = MarkAsReadRequest(ids)
        return apiClient.send(ServicesGateMethods.MARK_VIEWED.toString(), request, ResultOk::class.java)
    }

    override fun markAllEventsAsRead(): Either<ResultOk, ApiResponseError> {
        lockIfNeeded()
        return apiClient.send(ServicesGateMethods.MARK_VIEWED_ALL.toString(), MarkAllReadRequest(), ResultOk::class.java)
    }

    override fun getUnreadCount(profileId: String): Either<FreshResult, ApiResponseError> {
        lockIfNeeded()
        val request = GetUnreadCountRequest(profileId)

        return apiClient.send(ServicesGateMethods.GET_UNREAD_COUNT.toString(), request, FreshResult::class.java)
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

    private fun ContentParsingType.asContentType(): String {
        return when (this) {
            ContentParsingType.WEB -> "web"
            ContentParsingType.MOBILE -> "mobile"
            ContentParsingType.RAW -> "raw"
        }
    }


}