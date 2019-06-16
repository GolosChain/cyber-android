package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.*
import io.golos.sharedmodel.CyberName

@Abi
internal class VoteRequestAbi(private val voter: CyberName,
                              private val discussionIdAbi: DiscussionIdAbi,
                              private val weight: Short) {

    val getVoter: String
        @NameCompress get() = voter.name

    val getDiscussionId
        @ChildCompress get() = discussionIdAbi


    val getWeight: Short
        @ShortCompress get() = weight

    override fun toString(): String {
        return "VoteRequestAbi(voter=$voter, discussionIdAbi=$discussionIdAbi, weight=$weight)"
    }


}
