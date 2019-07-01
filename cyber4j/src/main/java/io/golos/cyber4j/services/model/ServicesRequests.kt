package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass

internal class ServicesRequestWrapper<T>(val method: String, val params: T)

@JsonClass(generateAdapter = true)
internal class DiscussionsRequests(val type: String,
                                   val sortBy: String,
                                   val sequenceKey: String?,
                                   val limit: Int,
                                   val userId: String?,
                                   val communityId: String?,
                                   val contentType: String)

@JsonClass(generateAdapter = true)
internal class DiscussionRequests(val userId: String,
                                  val permlink: String,
                                  val contentType: String)

@JsonClass(generateAdapter = true)
internal class UserMetaDataRequest(val userId: String)

internal class GetSecretRequest

@JsonClass(generateAdapter = true)
class AuthSecret(val secret: String)

@JsonClass(generateAdapter = true)
internal class ServicesAuthRequest(val user: String, val sign: String, val secret: String)

@JsonClass(generateAdapter = true)
internal class ResolveUserNameRequest(val username: String, val app: String)

enum class PostsFeedType {
    COMMUNITY, SUBSCRIPTIONS, USER_POSTS;

    override fun toString(): String {
        return when (this) {
            COMMUNITY -> "community"
            SUBSCRIPTIONS -> "subscriptions"
            USER_POSTS -> "byUser"
        }
    }
}

enum class CommentsOrigin {
    COMMENTS_OF_USER,
    COMMENTS_OF_POST,
    REPLIES;

    override fun toString(): String {

        return when (this) {
            COMMENTS_OF_USER -> "user"
            COMMENTS_OF_POST -> "post"
            REPLIES -> "replies"
        }
    }
}

enum class DiscussionTimeSort {
    SEQUENTIALLY, INVERTED;

    override fun toString(): String {
        return when (this) {
            SEQUENTIALLY -> "time"
            INVERTED -> "timeDesc"
        }
    }
}