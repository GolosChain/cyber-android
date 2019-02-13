package io.golos.commun4J.model

class CommunCommunity(val id: String, val name: String, private val avatarUrl: String?) {
    override fun toString(): String {
        return "CommunCommunity(id='$id', name='$name', avatarUrl=$avatarUrl)"
    }

    val getUrl: String?
        get() = if (avatarUrl == null || avatarUrl == "none") null else avatarUrl
}