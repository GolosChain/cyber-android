package io.golos.cyber4j.services.model


import io.golos.cyber4j.model.CyberDiscussion
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.model.DiscussionsResult
import io.golos.cyber4j.utils.Either

/** listener interface for auth state in cyber microservices.
 *
 * */

interface ApiService {

    fun getDiscussions(feedType: PostsFeedType,
                       sort: DiscussionTimeSort,
                       parsingType: ContentParsingType,
                       sequenceKey: String?,
                       limit: Int,
                       userId: String?,
                       communityId: String?): Either<DiscussionsResult, ApiResponseError>

    fun getPost(userId: String,
                permlink: String,
                parsingType: ContentParsingType): Either<CyberDiscussion, ApiResponseError>

    fun getComment(userId: String,
                   permlink: String,
                   parsingType: ContentParsingType): Either<CyberDiscussion, ApiResponseError>

    fun getComments(sort: DiscussionTimeSort,
                    sequenceKey: String?,
                    limit: Int,
                    origin: CommentsOrigin,
                    parsingType: ContentParsingType,
                    userId: String?,
                    permlink: String?): Either<DiscussionsResult, ApiResponseError>


    fun getUserMetadata(userId: String): Either<UserMetadataResult, ApiResponseError>

    fun getIframelyEmbed(forLink: String): Either<IFramelyEmbedResult, ApiResponseError>

    fun getOEmdedEmbed(forLink: String): Either<OEmbedResult, ApiResponseError>

    fun getRegistrationStateOf(userId: String?, phone: String?): Either<UserRegistrationStateResult, ApiResponseError>

    fun firstUserRegistrationStep(captcha: String?, phone: String, testingPass: String?): Either<FirstRegistrationStepResult, ApiResponseError>

    fun verifyPhoneForUserRegistration(phone: String, code: Int): Either<ResultOk, ApiResponseError>

    fun setVerifiedUserName(user: String, phone: String): Either<ResultOk, ApiResponseError>

    fun writeUserToBlockchain(userName: String, owner: String, active: String, posting: String, memo: String): Either<RegisterResult, ApiResponseError>

    fun resendSmsCode(name: String?, phone: String?): Either<ResultOk, ApiResponseError>

    fun waitBlock(blockNum: Long): Either<ResultOk, ApiResponseError>

    fun waitForTransaction(transactionId: String): Either<ResultOk, ApiResponseError>

    fun subscribeOnMobilePushNotifications(deviceId: String, fcmToken: String): Either<ResultOk, ApiResponseError>

    fun unSubscribeOnNotifications(deviceId: String, fcmToken: String): Either<ResultOk, ApiResponseError>

    fun setNotificationSettings(deviceId: String, newBasicSettings: Any?,
                                newWebNotifySettings: WebShowSettings?, newMobilePushSettings: MobileShowSettings?): Either<ResultOk, ApiResponseError>

    fun getNotificationSettings(deviceId: String): Either<UserSettings, ApiResponseError>

    fun getEvents(userProfile: String, afterId: String?, limit: Int?, markAsViewed: Boolean?, freshOnly: Boolean?, types: List<EventType>): Either<EventsData, ApiResponseError>

    fun markEventsAsRead(ids: List<String>): Either<ResultOk, ApiResponseError>

    fun markAllEventsAsRead(): Either<ResultOk, ApiResponseError>

    fun getUnreadCount(profileId: String): Either<FreshResult, ApiResponseError>

    fun getSubscriptions(ofUser: CyberName, limit: Int, type: SubscriptionType, sequenceKey: String?): Either<SubscriptionsResponse, ApiResponseError>

    fun getSubscribers(ofUser: CyberName, limit: Int, type: SubscriptionType, sequenceKey: String?): Either<SubscribersResponse, ApiResponseError>

    fun getAuthSecret(): Either<AuthSecret, ApiResponseError>

    fun authWithSecret(user: String,
                       secret: String,
                       signedSecret: String): Either<AuthResult, ApiResponseError>

    fun unAuth()

    fun resolveProfile(username: String,
                       appName: String): Either<ResolvedProfile, ApiResponseError>

}

