package io.golos.commun4J.model

data class CommunCommunity(val id: String, val name: String, private val avatarUrl: String?) {
    val getAvatarUrl: String? = if (avatarUrl == null || avatarUrl == "none") null else avatarUrl
}