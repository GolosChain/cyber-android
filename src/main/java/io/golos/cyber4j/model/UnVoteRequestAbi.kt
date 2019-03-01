package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class UnVoteRequestAbi(private val voter: CyberName,
                                private val discussionIdAbi: DiscussionIdAbi) {


    val getVoter: String
        @NameCompress get() = voter.name


    val getDiscussionId
        @ChildCompress get() = discussionIdAbi

    override fun toString(): String {
        return "UnVoteRequestAbi(voter=$voter, discussionIdAbi=$discussionIdAbi)"
    }
}
