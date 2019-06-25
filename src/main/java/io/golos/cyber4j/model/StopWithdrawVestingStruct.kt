package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
data class StopWithdrawVestingStruct(
        val owner: CyberName,
        val symbol: String = "") {
    val getOwner: String
        @NameCompress
        get() = owner.name

    val getSymbol: ByteArray
        @BytesCompress
        get() = byteArrayOf(6, 71, 79, 76, 79, 83, 0, 0)
}