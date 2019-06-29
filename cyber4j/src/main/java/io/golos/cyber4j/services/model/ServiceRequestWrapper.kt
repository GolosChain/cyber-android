package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass
import java.util.concurrent.atomic.AtomicLong

@JsonClass(generateAdapter = true)
class ServicesMessagesWrapper(val method: String,
                              val params: Any,
                              val jsonrpc: String = "2.0",
                              val id: Long = requestCounter.incrementAndGet())

private val requestCounter = AtomicLong(0)

