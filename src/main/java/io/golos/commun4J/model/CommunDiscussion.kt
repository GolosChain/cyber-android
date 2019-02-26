package io.golos.commun4J.model

import java.math.BigInteger
import java.util.*

data class CommunDiscussion(val contentId: DiscussionId,
                            val author: DiscussionAuthor,
                            val community: CommunCommunity,
                            val content: DiscussionContent,
                            val votes: DiscussionVotes,
                            val comments: DiscussionCommentsCount,
                            val payout: DiscussionPayout,
                            val postId: ParentId?,
                            val parentCommentId: ParentId?,
                            val meta: DiscussionMetadata)

data class DiscussionAuthor(val userId: CommunName, val username: String)

data class DiscussionId(val userId: String,
                        val permlink: String,
                        val refBlockNum: Int)

data class DiscussionCommentsCount(val count: Long)


data class DiscussionContent(val title: String, val body: ContentBody, val metadata: Any)

data class ContentBody(val preview: String?,
                       val full: String?)

data class DiscussionMetadata(val time: Date)

data class DiscussionPayout(val rShares: BigInteger)

data class DiscussionVotes(val hasUpVote: Boolean,
                           val hasDownVote: Boolean)

data class ParentId(val userId: CommunName, val permlink: String, val refBlockNum: Int)