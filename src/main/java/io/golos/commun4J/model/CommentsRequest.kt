package io.golos.commun4J.model

internal class CommentsRequest(val sortBy: String,
                               val sequenceKey: String?,
                               val limit: Int,
                               val type: String,
                               val userId: String?,
                               val permlink: String?,
                               val refBlockNum: Int?)