package io.golos.commun4J.model

import java.math.BigInteger
import java.util.*

class CommunDiscussion(val id: String,
                       val user: CommunUser,
                       val community: CommunCommunity,
                       val content: DiscussionContent,
                       val votes: DiscussionVotes,
                       val comments: DiscussionCommentsCount,
                       val payout: DiscussionPayout,
                       val meta: DiscussionMetadata,
                       val createdAt: Date,
                       val updatedAt: Date) {

    override fun toString(): String {
        return "CommunDiscussion(id='$id', user=$user, community=$community, content=$content, votes=$votes, comments=$comments, payout=$payout, meta=$meta, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}

class DiscussionCommentsCount(val count: Long) {
    override fun toString(): String {
        return "DiscussionCommentsCount(count=$count)"
    }
}


class DiscussionContent(val title: String, val body: ContentBody) {

    override fun toString(): String {
        return "DiscussionContent(title='$title', body=$body)"
    }
}

class ContentBody(val preview: String?,
                  val full: String?) {
    override fun toString(): String {
        return "ContentBody(preview=$preview, full=$full)"
    }
}

class DiscussionMetadata(val time: Date) {
    override fun toString(): String {
        return "DiscussionMetadata(time='$time')"
    }
}

class DiscussionPayout(val rShares: BigInteger) {
    override fun toString(): String {
        return "DiscussionPayout(rShares=$rShares)"
    }
}

class DiscussionVotes(private val upUserIdList: List<String>?,
                      private val downUserIdList: List<String>?,
                      val upByUser: Boolean,
                      val downByUser: Boolean) {
    override fun toString(): String {
        return "DiscussionVotes(upUserIdList=$upUserIdList, downUserIdList=$downUserIdList, upByUser=$upByUser, downByUser=$downByUser)"
    }

    val getUpvotedUsers: List<String>
        get() = upUserIdList ?: emptyList()

    val getDownvotedUsers: List<String>
        get() = downUserIdList ?: emptyList()
}