package io.golos.commun4J.services

import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.model.*
import io.golos.commun4J.services.model.ApiResponseError
import io.golos.commun4J.services.model.DiscussionRequests
import io.golos.commun4J.services.model.DiscussionsRequests
import io.golos.commun4J.services.model.UserMetaDataRequest
import io.golos.commun4J.utils.Either

private enum class ServicesGateMethods {
    GET_FEED, GET_POST, GET_COMMENTS, GET_USER_METADATA;

    override fun toString(): String {
        return when (this) {
            GET_FEED -> "content.getFeed"
            GET_POST -> "content.getPost"
            GET_COMMENTS -> "content.getComments"
            GET_USER_METADATA -> "content.getProfile"
        }
    }
}

internal class CommunServicesApiProvider(val config: Commun4JConfig,
                                         val apiClient: ApiClient = CommunServicesWebSocketClient()) : HistoryApiProvider {
    private val communClient = CommunServicesWebSocketClient()


    override fun getDiscussions(feedType: PostsFeedType,
                                sort: DiscussionTimeSort,
                                sequenceKey: String?,
                                limit: Int,
                                userId: String?,
                                communityId: String?): Either<DiscussionsResult, ApiResponseError> {

        communClient.connect(config)
        return communClient.send(ServicesGateMethods.GET_FEED.toString(),
                DiscussionsRequests(feedType.toString(),
                        sort.toString(),
                        sequenceKey,
                        limit,
                        userId,
                        communityId), DiscussionsResult::class.java)
    }

    override fun getDiscussion(userId: String, permlink: String, refBlockNum: Int): Either<CommunDiscussion, ApiResponseError> {
        communClient.connect(config)
        return communClient.send(ServicesGateMethods.GET_POST.toString(), DiscussionRequests(userId, permlink, refBlockNum), CommunDiscussion::class.java)
    }

    override fun getComments(sort: DiscussionTimeSort, sequenceKey: String?,
                             limit: Int, origin: CommentsOrigin, userId: String?,
                             permlink: String?, refBlockNum: Int?): Either<DiscussionsResult, ApiResponseError> {
        communClient.connect(config)
        return communClient.send(ServicesGateMethods.GET_COMMENTS.toString(),
                CommentsRequest(sort.toString(), sequenceKey, limit, origin.toString(), userId, permlink, refBlockNum), DiscussionsResult::class.java)
    }

    override fun getUserMetadata(userId: String): Either<UserMetadata, ApiResponseError> {
        communClient.connect(config)
        return communClient.send(ServicesGateMethods.GET_USER_METADATA.toString(),
                UserMetaDataRequest(userId), UserMetadata::class.java)
    }
}