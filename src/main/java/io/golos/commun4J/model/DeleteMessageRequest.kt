package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class DeleteMessageRequest(private val author: CommunName,
                           private val permlink: String) {


    val getAuthor: String
        @NameCompress get() = author.name

    val getPermlink: String
        @StringCompress get() = permlink

}
