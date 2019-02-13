package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class UnVoteRequest(private val voter: CommunName,
                    private val author: CommunName,
                    val permlink: String) {


    val getVoter: String
        @NameCompress get() = voter.name


    val getAuthor: String
        @NameCompress get() = author.name

    val getPermlink: String
        @StringCompress get() = permlink


    override fun toString(): String {
        return "VoteRequest{" +
                "voter=" + voter +
                ", author=" + author +
                ", permlink='" + permlink + '\''.toString() +
                '}'.toString()
    }
}
