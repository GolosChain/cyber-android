package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass
import io.golos.sharedmodel.CyberName

@JsonClass(generateAdapter = true)
internal class SubscribersRequest(val userId: CyberName,
                                  val limit: Int,
                                  val type: String,
                                  val sequenceKey: String?)