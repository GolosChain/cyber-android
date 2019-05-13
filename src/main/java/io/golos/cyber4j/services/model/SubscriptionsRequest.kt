package io.golos.cyber4j.services.model

import io.golos.cyber4j.model.CyberName

enum class SubscriptionType {
    USER, COMMUNITY;

    override fun toString(): String {
        return when (this) {
            USER -> "user"
            COMMUNITY -> "community"
        }
    }
}

internal class SubscriptionsRequest(val userId: CyberName,
                                    val limit: Int,
                                    subscriptionType: SubscriptionType,
                                    val sequenceKey: String?) {
    val type: String = subscriptionType.toString()
}