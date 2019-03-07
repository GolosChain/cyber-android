package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class VestingStartRequestAbi(private val owner: CyberName,
                                      private val ramPayer: CyberName,
                                      private val precision: Byte) {

    val getOwner: String
        @NameCompress get() = owner.name

    val getPrecision: Byte
        @ByteCompress get() = precision

    val decsBytes: ByteArray
        @BytesCompress
        get() = byteArrayOf(71, 76, 83, 0, 0, 0, 0)

    val getRamPayer: String
        @NameCompress
        get() = ramPayer.name


}