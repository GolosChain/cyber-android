package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class ReblogRequestAbi(val reblogber: CyberName,
                                val discussionIdAbi: DiscussionIdAbi,
                                val title: String,
                                val body: String) {

    val getReblogger: String
        @NameCompress get() = reblogber.name

    val getDiscussionIdAbi: DiscussionIdAbi
        @ChildCompress get() = discussionIdAbi
    val getTitle: String
        @StringCompress get() = title
    val getBody: String
        @StringCompress get() = body
}