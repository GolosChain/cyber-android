package io.golos.commun4J.services.model

internal class ServicesRequestWrapper<T>(val method: String, val params: T)

internal class DiscussionsRequests(val type: String,
                                   val sortBy: String,
                                   val sequenceKey: String?,
                                   val limit: Int,
                                   val userId: String?,
                                   val communityId: String?)

internal class DiscussionRequests(val userId: String,
                                  val permlink: String,
                                  val refBlockNum: Int)

internal class UserMetaDataRequest(val userId: String)

internal class GetSecretRequest {
    val params = Any()
}

internal class CommunAuthRequest(val user: String, val sign: String, val secret: String)