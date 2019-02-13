package io.golos.commun4J.model

import io.golos.commun4J.services.model.ApiResponseError
import io.golos.commun4J.utils.Either


interface HistoryApiProvider {
    fun getDiscussions(): Either<List<CommunDiscussion>, ApiResponseError>
    fun getDiscussion(id: String): Either<CommunDiscussion, ApiResponseError>
}

