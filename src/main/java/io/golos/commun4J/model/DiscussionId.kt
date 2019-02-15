package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
class DiscussionId(private val name: CommunName,
                   private val permLink: String,
                   private val refBlockNum: Long) {
    val getName: String
        @NameCompress get() = name.name

    val getPermLink: String
        @StringCompress get() = permLink

    val getRefBlockNum: Long
        @LongCompress get() = refBlockNum

    override fun toString(): String {
        return "DiscussionId(name=$name, permLink='$permLink', refBlockNum=$refBlockNum)"
    }

}