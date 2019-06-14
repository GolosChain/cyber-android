package io.golos.cyber4j.services.model

import java.util.concurrent.atomic.AtomicLong


class ServicesMessagesWrapper(val method: String,
                              val params: Any) {

    val jsonrpc = "2.0"

    @Transient
    private val _id = requestCounter.incrementAndGet()

    val id = _id
}

private val requestCounter = AtomicLong(0)

