package io.golos.commun4J.services.model

import java.util.concurrent.atomic.AtomicLong


class ServicesMessagesWrapper(val method: String,
                              val params: String) {

    val jsonRpc = "2.0"

    @Transient
    private val _id = requestCounter.incrementAndGet()

    val id = _id
}

private val requestCounter = AtomicLong(0)

