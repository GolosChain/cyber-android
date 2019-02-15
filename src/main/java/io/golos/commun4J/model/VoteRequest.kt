package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class VoteRequest(private val voter: CommunName,
                           private val discussionId: DiscussionId,
                           private val weight: Short) {

    val getVoter: String
        @NameCompress get() = voter.name

    val getDiscussionId
        @ChildCompress get() = discussionId


    val getWeight: Int
        @IntCompress get() = weight.toInt()

    override fun toString(): String {
        return "VoteRequest(voter=$voter, discussionId=$discussionId, weight=$weight)"
    }


}
