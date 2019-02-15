package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class UnVoteRequest(private val voter: CommunName,
                             private val discussionId: DiscussionId) {


    val getVoter: String
        @NameCompress get() = voter.name


    val getDiscussionId
        @ChildCompress get() = discussionId

    override fun toString(): String {
        return "UnVoteRequest(voter=$voter, discussionId=$discussionId)"
    }
}
