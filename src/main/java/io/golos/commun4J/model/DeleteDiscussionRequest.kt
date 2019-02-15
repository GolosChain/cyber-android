package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress

@Abi
internal class DeleteDiscussionRequest(private val discussionId: DiscussionId) {


    val getDiscussionId: DiscussionId
        @ChildCompress get() = discussionId

}
