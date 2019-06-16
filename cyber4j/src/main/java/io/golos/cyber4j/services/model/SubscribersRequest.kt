package io.golos.cyber4j.services.model

import io.golos.sharedmodel.CyberName


internal class SubscribersRequest(val userId: CyberName,
                                  val limit: Int,
                                  subscriptionType: SubscriptionType,
                                  val sequenceKey: String?) {
    val type: String = subscriptionType.toString()
}