package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import io.golos.sharedmodel.CyberName

@Abi
internal class BlockUserRequestAbi(private val blocker: CyberName,
                                   private val blocking: CyberName) {

    val getblocker
        @NameCompress get() = blocker.name

    val getblocking
        @NameCompress get() = blocking.name

    override

    fun toString(): String {
        return "BlockUserRequestAbi(blocker=$blocker, blocking=$blocking)"
    }
}