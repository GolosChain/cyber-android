package io.golos.commun4J.model

class CommunCommunity(val id: String, val name: String, val avatarUrl: String?) {
    override fun toString(): String {
        return "CommunCommunity(id='$id', name='$name', avatarUrl=$avatarUrl)"
    }
}