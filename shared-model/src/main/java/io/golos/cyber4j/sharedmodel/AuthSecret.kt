package io.golos.cyber4j.sharedmodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AuthSecret(val secret: String)