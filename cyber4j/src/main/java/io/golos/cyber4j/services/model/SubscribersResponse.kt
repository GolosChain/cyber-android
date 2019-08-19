package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass
import io.golos.cyber4j.sharedmodel.CyberName

@JsonClass(generateAdapter = true)
data class SubscribersResponse(var items: List<CyberName>, var sequenceKey: String?)
