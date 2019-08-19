package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass
import io.golos.cyber4j.sharedmodel.CyberName

@JsonClass(generateAdapter = true)
data class AuthResult(val user: CyberName, val displayName: String?, val roles: Array<String>,
                      val permission: String)
