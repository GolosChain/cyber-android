package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ServicesResponseWrapper<T>(val id: Long, val result: T)

@JsonClass(generateAdapter = true)
internal class Identifieble(val id: Long)