package io.golos.cyber4j.model

import io.golos.cyber4j.services.model.CyberCommunity
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

data class DiscussionAuthor(val userId: CyberName, val username: String, val avatarUrl: String)

data class DiscussionId(
        val userId: String,
        val permlink: String,
        val refBlockNum: Long
)


data class DiscussionStats(val commentsCount: Long, val wilson: DiscussionWilson)

data class DiscussionWilson(val hot: Double, val trending: Double)

data class DiscussionContent(val title: String?,
                             val body: ContentBody,
                             val tags: List<String>,
                             val embeds: List<Embed>)



data class ContentBody(
        val preview: String?,
        val full: String?,
        val raw: String?,
        val mobile: List<ContentRow>?
)

data class Embed(val _id: String?,
                 val id: String?,
                 val type: String?,
                 val result: EmbedResult)

data class EmbedResult(val type: String?,
                       val version: String?,
                       val title: String?,
                       val url: String?,
                       val author: String?,
                       val author_url: String?,
                       val provider_name: String?,
                       val description: String?,
                       val thumbnail_url: String?,
                       val thumbnail_width: Int?,
                       val thumbnail_height: Int?,
                       val height: Int?,
                       val html: String?)

data class DiscussionMetadata(val time: Date)

data class DiscussionPayout(val rShares: BigInteger)

data class DiscussionVotes(
        val hasUpVote: Boolean,
        val hasDownVote: Boolean,
        val upCount: Int,
        val downCount: Int
)

sealed class ContentRow

data class TextRow(val content: String,
                   val type: String = typeName) : ContentRow() {
    companion object {
        const val typeName = "text"
    }
}


data class ImageRow(val src: String,
                    val type: String = typeName) : ContentRow() {
    companion object {
        const val typeName = "image"
    }
}


data class Parent(val post: ParentContentId?, val comment: ParentContentId?)

data class ParentContentId(val contentId: DiscussionId)