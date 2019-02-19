package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress

@Abi
class PinRequest(private val pinner: CommunName, private val pinning: CommunName) {
    val getPinner: String
        @NameCompress get() = pinner.name

    val getPinning: String
        @NameCompress get() = pinning.name

    override fun toString(): String {
        return "PinRequest(pinner=$pinner, pinning=$pinning)"
    }
}