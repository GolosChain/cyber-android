package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress

@Abi
internal class DeleteDiscussionRequestAbi(private val discussionIdAbi: DiscussionIdAbi) {


    val getDiscussionIdAbi: DiscussionIdAbi
        @ChildCompress get() = discussionIdAbi

}