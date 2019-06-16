package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress
import io.golos.sharedmodel.CyberName

@Abi
internal class DiscussionIdAbi(private val name: CyberName,
                               private val permLink: String) {
    val getName: String
        @NameCompress get() = name.name

    val getPermLink: String
        @StringCompress get() = permLink

    override fun toString(): String {
        return "DiscussionIdAbi(name=$name, permLink='$permLink'"
    }

}