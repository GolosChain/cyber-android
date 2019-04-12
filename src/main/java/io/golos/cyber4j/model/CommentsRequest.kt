package io.golos.cyber4j.model

import com.squareup.moshi.Json

internal class CommentsRequest(val sortBy: String,
                               val sequenceKey: String?,
                               val limit: Int,
                               @Json(name = "contentType")
                               val contentType: String,
                               val type: String,
                               val userId: String?,
                               val permlink: String?,
                               val refBlockNum: Long?)