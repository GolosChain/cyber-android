package io.golos.cyber4j.model

data class CyberCommunity(val id: String, val name: String, private val avatarUrl: String?) {
    val getAvatarUrl: String? = if (avatarUrl == null || avatarUrl == "none") null else avatarUrl
}