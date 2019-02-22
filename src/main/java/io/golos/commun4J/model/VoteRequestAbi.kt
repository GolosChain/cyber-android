package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class VoteRequestAbi(private val voter: CommunName,
                              private val discussionIdAbi: DiscussionIdAbi,
                              private val weight: Short) {

    val getVoter: String
        @NameCompress get() = voter.name

    val getDiscussionId
        @ChildCompress get() = discussionIdAbi


    val getWeight: Int
        @IntCompress get() = weight.toInt()

    override fun toString(): String {
        return "VoteRequestAbi(voter=$voter, discussionIdAbi=$discussionIdAbi, weight=$weight)"
    }


}
