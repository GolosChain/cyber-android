package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress

@Abi
class BlockUserRequest(private val blocker: CommunName,
                       private val blocking: CommunName) {

    val getblocker
        @NameCompress get() = blocker.name

    val getblocking
        @NameCompress get() = blocking.name

    override

    fun toString(): String {
        return "BlockUserRequest(blocker=$blocker, blocking=$blocking)"
    }
}