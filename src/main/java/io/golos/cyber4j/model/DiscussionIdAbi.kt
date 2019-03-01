package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class DiscussionIdAbi(private val name: CyberName,
                               private val permLink: String,
                               private val refBlockNum: Long) {
    val getName: String
        @NameCompress get() = name.name

    val getPermLink: String
        @StringCompress get() = permLink

    val getRefBlockNum: Long
        @LongCompress get() = refBlockNum

    override fun toString(): String {
        return "DiscussionIdAbi(name=$name, permLink='$permLink', refBlockNum=$refBlockNum)"
    }

}