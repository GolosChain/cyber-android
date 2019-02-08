package io.golos.commun4J.model

class CommunDiscussion(val id: String,
                       val user: CommunUser,
                       val community: CommunCommunity,
                       val content: CommunContent,
                       val votes: Any,
                       val comments: CommentsCount,
                       val payout: Payout,
                       val meta: DiscussionMetadata)

class CommentsCount(val count: Long)


