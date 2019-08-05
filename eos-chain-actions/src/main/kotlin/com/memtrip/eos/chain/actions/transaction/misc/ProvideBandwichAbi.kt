package com.memtrip.eos.chain.actions.transaction.misc

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import io.golos.sharedmodel.CyberName

@Abi
data class ProvideBandwichAbi(private val provider: CyberName,
                              private val recipient: CyberName) {
    val getProvider: String
        @NameCompress get() = provider.name
    val getRecipient
        @NameCompress get() = recipient.name
}