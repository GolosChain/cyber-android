package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class EmbedRequest(val type: String, val url: String)
