package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass
import io.golos.cyber4j.sharedmodel.CyberName

@JsonClass(generateAdapter = true)
class ResolvedProfile(var userId: CyberName, var username: String?, var avatarUrl: String?)