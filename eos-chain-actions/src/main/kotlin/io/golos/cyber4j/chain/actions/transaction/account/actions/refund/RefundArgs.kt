package io.golos.cyber4j.chain.actions.transaction.account.actions.refund

import io.golos.cyber4j.abi.writer.Abi
import io.golos.cyber4j.abi.writer.AccountNameCompress

@Abi
data class RefundArgs(
    val owner: String
) {

    val getOwner: String
        @AccountNameCompress get() = owner
}