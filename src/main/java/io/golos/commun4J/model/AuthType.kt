package io.golos.commun4J.model

enum class AuthType { ACTIVE;

    override fun toString(): String {
        return  when(this){
            ACTIVE -> "active"
        }
    }
}