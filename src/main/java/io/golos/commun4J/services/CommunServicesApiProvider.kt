package io.golos.commun4J.services

import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.model.CommunDiscussion
import io.golos.commun4J.model.HistoryApiProvider
import io.golos.commun4J.services.model.ApiResponseError
import io.golos.commun4J.services.model.DiscussionRequests
import io.golos.commun4J.services.model.DiscussionsRequests
import io.golos.commun4J.utils.Either

internal class CommunServicesApiProvider(val config: Commun4JConfig,
                                         val apiClient: ApiClient = CommunServicesWebSocketClient()) : HistoryApiProvider {
    private val communClient = CommunServicesWebSocketClient()

    override fun getDiscussions(): Either<List<CommunDiscussion>, ApiResponseError> {
        communClient.connect(config)
        return communClient.send("content.getFeed",
                DiscussionsRequests("GOLOSID"), CommunDiscussion::class.java, true) as Either<List<CommunDiscussion>, ApiResponseError>
    }

    override fun getDiscussion(id: String): Either<CommunDiscussion, ApiResponseError> {
        communClient.connect(config)
        return communClient.send("content.getPost", DiscussionRequests(id), CommunDiscussion::class.java)
    }
}