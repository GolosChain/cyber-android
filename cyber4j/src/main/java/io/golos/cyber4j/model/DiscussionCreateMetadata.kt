package io.golos.cyber4j.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DiscussionCreateMetadata(var embeds: List<EmbedmentsUrl>,
                                    var tags: List<String>)

@JsonClass(generateAdapter = true)
data class EmbedmentsUrl(var url: String)
