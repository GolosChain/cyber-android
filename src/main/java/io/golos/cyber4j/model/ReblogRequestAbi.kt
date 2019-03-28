package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class ReblogRequestAbi(val reblogber: CyberName, val discussionIdAbi: DiscussionIdAbi) {

    val getReblogger: String
        @NameCompress get() = reblogber.name

    val getDiscussionIdAbi: DiscussionIdAbi
        @ChildCompress get() = discussionIdAbi
}