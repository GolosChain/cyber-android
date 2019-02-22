package io.golos.commun4J.services.model

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