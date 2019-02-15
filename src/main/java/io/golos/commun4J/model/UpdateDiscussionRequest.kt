package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class UpdateDiscussionRequest(
        private val discussionId: DiscussionId,
        private val title: String,
        private val body: String,
        private val tags: List<Tag>,
        private val language: String = "ru",
        private val jsonmetadata: String) {


    val getDiscussionId: DiscussionId
        @ChildCompress get() = discussionId

    val getHeadermssg: String
        @StringCompress get() = title

    val getBodyMessage: String
        @StringCompress get() = body
    val getLanguage: String
        @StringCompress get() = language

    val getTags: List<Tag>
        @CollectionCompress get() = tags

    val getJsonmetadata: String
        @StringCompress get() = jsonmetadata

    override fun toString(): String {
        return "UpdateDiscussionRequest(discussionId=$discussionId, title='$title', body='$body', tags=$tags, language='$language', jsonmetadata='$jsonmetadata')"
    }


}
