package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class UpdatePostRequest(private val postAuthor: CommunName,
                        private val permlink: String,
                        private val title: String,
                        private val body: String,
                        private val tags: List<Tag>,
                        private val language: String = "ru",
                        private val jsonmetadata: String) {


    val getPostAuthor: String
        @NameCompress get() = postAuthor.name
    val getPermlink: String
        @StringCompress get() = permlink

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
        return "UpdatePostRequest{" +
                "postAuthor=" + postAuthor +
                ", permlink='" + permlink + '\''.toString() +
                ", title='" + title + '\''.toString() +
                ", body='" + body + '\''.toString() +
                ", language='" + language + '\''.toString() +
                ", tags=" + tags +
                ", jsonmetadata='" + jsonmetadata + '\''.toString() +
                '}'.toString()
    }
}
