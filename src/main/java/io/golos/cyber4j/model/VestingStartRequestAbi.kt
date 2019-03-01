package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class VestingStartRequestAbi(private val owner: CyberName,
                                      private val ramPayer: CyberName) {

    val getOwner: String
        @NameCompress get() = owner.name

    val decsBytes: ByteArray
        @BytesCompress
        get() = byteArrayOf(3, 71, 76, 83, 0, 0, 0, 0)

    val getRamPayer: String
        @NameCompress
        get() = ramPayer.name


}