package io.golos.commun4J.model


interface HistoryApiProvider {
    fun getDiscussions(): List<CommunDiscussion>
    fun getDiscussion(id: String): CommunDiscussion
}

