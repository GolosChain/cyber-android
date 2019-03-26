package io.golos.cyber4j.model

import java.math.BigInteger
import java.util.*

data class CyberDiscussion(
    val contentId: DiscussionId,
    val author: DiscussionAuthor?,
    val community: CyberCommunity?,
    val content: DiscussionContent,
    val votes: DiscussionVotes,
    val stats: DiscussionStats?,
    val payout: DiscussionPayout,
    val parent: Parent?,
    val meta: DiscussionMetadata
)

data class DiscussionAuthor(val userId: CyberName, val username: String)

data class DiscussionId(
    val userId: String,
    val permlink: String,
    val refBlockNum: Long
)


data class DiscussionStats(val commentsCount: Long, val wilson: DiscussionWilson)

data class DiscussionWilson(val hot: Double, val trending: Double)

data class DiscussionContent(val title: String?, val body: ContentBody, val metadata: Any?)

data class ContentBody(
    val preview: String?,
    val full: String?
)

data class DiscussionMetadata(val time: Date)

data class DiscussionPayout(val rShares: BigInteger)

data class DiscussionVotes(
    val hasUpVote: Boolean,
    val hasDownVote: Boolean,
    val upCount: Int,
    val downCount: Int
)

data class Parent(val post: ParentContentId?, val comment: ParentContentId?)

data class ParentContentId(val contentId: DiscussionId)