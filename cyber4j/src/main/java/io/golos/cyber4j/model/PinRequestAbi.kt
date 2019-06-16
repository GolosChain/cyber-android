package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import io.golos.sharedmodel.CyberName

@Abi
internal class PinRequestAbi(private val pinner: CyberName, private val pinning: CyberName) {
    val getPinner: String
        @NameCompress get() = pinner.name

    val getPinning: String
        @NameCompress get() = pinning.name

    override fun toString(): String {
        return "PinRequestAbi(pinner=$pinner, pinning=$pinning)"
    }
}