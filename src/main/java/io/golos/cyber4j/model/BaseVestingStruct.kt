package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.NameCompress
import com.squareup.moshi.JsonClass

@Abi
data class BaseTransferVestingStruct(
        val from: CyberName,
        val to: CyberName,
        val quantity: String
) {
    val getFrom: String
        @NameCompress
        get() = from.name

    val getTo: String
        @NameCompress
        get() = to.name

    val getQuantity: String
        @AssetCompress
        get() = quantity
}