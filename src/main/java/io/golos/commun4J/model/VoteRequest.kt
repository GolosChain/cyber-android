package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class VoteRequest(private val name: CommunName,
                           private val author: CommunName,
                           private val permlink: String,
                           private val weight: Short) {

    val getName: String
        @NameCompress get() = name.name


    val getAuthor: String
        @NameCompress get() = author.name

    val getPermlink: String
        @StringCompress get() = permlink

    val getWeight: Int
        @IntCompress get() = weight.toInt()


    override fun toString(): String {
        return "VoteRequest{" +
                "name=" + name +
                ", author=" + author +
                ", permlink='" + permlink + '\''.toString() +
                ", weight=" + weight +
                '}'.toString()
    }
}
