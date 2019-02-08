package io.golos.commun4J.services

import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.model.CommunDiscussion
import io.golos.commun4J.model.HistoryApiProvider

class CommunServicesApiProvider(val config: Commun4JConfig) : HistoryApiProvider {
    private val communClient = CommunServicesWebSocketClient

    override fun getDiscussions(): List<CommunDiscussion> {
        communClient.connect(config)
        return communClient.send("content.getFeed",
                "{\"communityId\": \"GOLOSID\"}", List::class.java) as List<CommunDiscussion>
    }

    override fun getDiscussion(id: String): CommunDiscussion {
        communClient.connect(config)
        return communClient.send("content.getPost",
                "{\"postId\":$id}", CommunDiscussion::class.java)
    }
}