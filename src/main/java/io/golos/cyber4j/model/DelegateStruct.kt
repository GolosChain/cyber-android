package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.*
import com.squareup.moshi.JsonClass

@Abi
data class DelegateVestingStruct(
        val from: CyberName,
        val to: CyberName,
        val quantity: String,
        val interest_rate: Short,
        val payout_strategy: Byte
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

    val getInterestRate: Short
        @ShortCompress
        get() = interest_rate

    val getPayout_strategy: Byte
        @ByteCompress get() = payout_strategy
}