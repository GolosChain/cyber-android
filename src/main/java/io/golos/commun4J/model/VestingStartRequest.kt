package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class VestingStartRequest(private val owner: CommunName) {

    val getOwner: String
        @NameCompress get() = owner.name

    val decsBytes: ByteArray
        @BytesCompress
        get() = byteArrayOf(3, 71, 76, 83, 0, 0, 0, 0)

    val ramPayer: String
        @NameCompress
        get() = "eosio"


}