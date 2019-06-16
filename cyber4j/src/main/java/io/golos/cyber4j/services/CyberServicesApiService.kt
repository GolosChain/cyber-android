package io.golos.cyber4j.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.*
import io.golos.cyber4j.services.model.*
import io.golos.cyber4j.utils.*
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberNameAdapter
import java.math.BigInteger
import java.util.*

private enum class ServicesGateMethods {
    GET_FEED, GET_POST, GET_COMMENT, GET_COMMENTS, GET_USER_METADATA, GET_SECRET, AUTH, GET_EMBED,
    GET_REGISTRATION_STATE, REG_FIRST_STEP, REG_VERIFY_PHONE, REG_SET_USER_NAME, REG_WRITE_TO_BLOCKCHAIN,
    REG_RESEND_SMS, WAIT_BLOCK, WAIT_FOR_TRANSACTION, PUSH_SUBSCRIBE, PUSH_UNSUBSCRIBE, GET_NOTIFS_HISTORY, MARK_VIEWED,
    GET_UNREAD_COUNT, MARK_VIEWED_ALL, SET_SETTINGS, GET_SETTINGS, GET_SUBSCRIPTIONS, GET_SUBSCRIBERS,
    RESOLVE_USERNAME;

    override fun toString(): String {
        return when (this) {
            GET_FEED -> "content.getFeed"
            GET_POST -> "content.getPost"
            GET_COMMENT -> "content.getComment"
            GET_COMMENTS -> "content.getComments"
            WAIT_BLOCK -> "content.waitForBlock"
            WAIT_FOR_TRANSACTION -> "content.waitForTransaction"
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
            GET_SUBSCRIPTIONS -> "content.getSubscriptions"
            GET_SUBSCRIBERS -> "content.getSubscribers"
            RESOLVE_USERNAME -> "content.resolveProfile"
        }
    }
}


internal class CyberServicesApiService(
        private val config: Cyber4JConfig,
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
                        .add(CyberName::class.java, CyberNameAdapter())
                        .add(ServiceSettingsLanguage::class.java, ServiceSettingsLanguageAdapter())
                        .add(EventsAdapter())
                        .build()
        )
) :
        ApiService {

    override fun getAuthSecret(): Either<AuthSecret, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.GET_SECRET.toString(),
                GetSecretRequest(), AuthSecret::class.java
        )
    }

    override fun authWithSecret(user: String,
                                secret: String,
                                signedSecret: String): Either<AuthResult, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.AUTH.toString(),
                ServicesAuthRequest(
                        user,
                        signedSecret,
                        secret
                ), AuthResult::class.java)
    }

    override fun unAuth() {
        apiClient.unAuth()
    }

    override fun resolveProfile(username: String, appName: String): Either<ResolvedProfile, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.RESOLVE_USERNAME.toString(),
                ResolveUserNameRequest(
                        username,
                        appName
                ), ResolvedProfile::class.java)
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
            parsingType: ContentParsingType
    ): Either<CyberDiscussion, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.GET_POST.toString(),
                DiscussionRequests(userId, permlink, parsingType.asContentType()),
                CyberDiscussion::class.java
        )
    }

    override fun waitBlock(blockNum: Long): Either<ResultOk, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.WAIT_BLOCK.toString(),
                WaitRequest(blockNum, null),
                ResultOk::class.java
        )
    }

    override fun waitForTransaction(transactionId: String): Either<ResultOk, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.WAIT_FOR_TRANSACTION.toString(),
                WaitRequest(null, transactionId),
                ResultOk::class.java
        )
    }

    override fun getComment(
            userId: String,
            permlink: String,
            parsingType: ContentParsingType
    ): Either<CyberDiscussion, ApiResponseError> {

        return apiClient.send(
                ServicesGateMethods.GET_COMMENT.toString(), DiscussionRequests(
                userId,
                permlink,
                parsingType.asContentType()
        ), CyberDiscussion::class.java
        )
    }

    override fun getComments(
            sort: DiscussionTimeSort, sequenceKey: String?,
            limit: Int, origin: CommentsOrigin,
            parsingType: ContentParsingType,
            userId: String?,
            permlink: String?
    ): Either<DiscussionsResult, ApiResponseError> {


        return apiClient.send(
                ServicesGateMethods.GET_COMMENTS.toString(),
                CommentsRequest(
                        sort.toString(),
                        sequenceKey, limit,
                        parsingType.asContentType(),
                        origin.toString(),
                        userId,
                        permlink
                ), DiscussionsResult::class.java
        )
    }

    override fun getSubscriptions(ofUser: CyberName, limit: Int, type: SubscriptionType, sequenceKey: String?): Either<SubscriptionsResponse, ApiResponseError> {
        return apiClient.send(ServicesGateMethods.GET_SUBSCRIPTIONS.toString(),
                SubscriptionsRequest(ofUser, limit, type, sequenceKey), SubscriptionsResponse::class.java)
    }

    override fun getSubscribers(ofUser: CyberName, limit: Int, type: SubscriptionType, sequenceKey: String?): Either<SubscribersResponse, ApiResponseError> {
        return apiClient.send(ServicesGateMethods.GET_SUBSCRIBERS.toString(),
                SubscribersRequest(ofUser, limit, type, sequenceKey), SubscribersResponse::class.java)
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

    override fun getUserMetadata(userId: String): Either<UserMetadataResult, ApiResponseError> {

        return apiClient.send(
                ServicesGateMethods.GET_USER_METADATA.toString(),
                UserMetaDataRequest(userId), UserMetadataResult::class.java
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
    ): Either<RegisterResult, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.REG_WRITE_TO_BLOCKCHAIN.toString(),
                WriteUserToBlockchainRequest(userName, owner, active, posting, memo),
                RegisterResult::class.java
        )
    }

    override fun resendSmsCode(name: String?, phone: String?): Either<ResultOk, ApiResponseError> {
        return apiClient.send(
                ServicesGateMethods.REG_RESEND_SMS.toString(),
                ResendUserSmsRequest(name, phone), ResultOk::class.java
        )
    }

    override fun subscribeOnMobilePushNotifications(deviceId: String, fcmToken: String): Either<ResultOk, ApiResponseError> {

        val request = PushSubscibeRequest(fcmToken, deviceId)
        return apiClient.send(
                ServicesGateMethods.PUSH_SUBSCRIBE.toString(),
                request, ResultOk::class.java
        )
    }

    override fun unSubscribeOnNotifications(deviceId: String, fcmToken: String): Either<ResultOk, ApiResponseError> {

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

        val request = UserSettings(deviceId, newBasicSettings, newWebNotifySettings, newMobilePushSettings)

        return apiClient.send(
                ServicesGateMethods.SET_SETTINGS.toString(),
                request, ResultOk::class.java
        )
    }

    override fun getNotificationSettings(deviceId: String): Either<UserSettings, ApiResponseError> {

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

        val request = EventsRequest(userProfile, afterId, limit, types, markAsViewed, freshOnly)

        return apiClient.send(
                ServicesGateMethods.GET_NOTIFS_HISTORY.toString(),
                request, EventsData::class.java
        )
    }

    override fun markEventsAsRead(ids: List<String>): Either<ResultOk, ApiResponseError> {

        val request = MarkAsReadRequest(ids)
        return apiClient.send(ServicesGateMethods.MARK_VIEWED.toString(), request, ResultOk::class.java)
    }

    override fun markAllEventsAsRead(): Either<ResultOk, ApiResponseError> {

        return apiClient.send(ServicesGateMethods.MARK_VIEWED_ALL.toString(), MarkAllReadRequest(), ResultOk::class.java)
    }

    override fun getUnreadCount(profileId: String): Either<FreshResult, ApiResponseError> {

        val request = GetUnreadCountRequest(profileId)

        return apiClient.send(ServicesGateMethods.GET_UNREAD_COUNT.toString(), request, FreshResult::class.java)
    }

    override fun shutDown() {
        apiClient.unAuth()
    }

    private fun ContentParsingType.asContentType(): String {
        return when (this) {
            ContentParsingType.WEB -> "web"
            ContentParsingType.MOBILE -> "mobile"
            ContentParsingType.RAW -> "raw"
        }
    }
}