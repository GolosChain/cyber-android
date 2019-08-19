package io.golos.cyber4j.chain.actions.transaction.account.actions.refund

import io.golos.cyber4j.abi.writer.Abi
import io.golos.cyber4j.abi.writer.ChildCompress

@Abi
data class RefundBody(
    val args: RefundArgs
) {

    val getArgs: RefundArgs
        @ChildCompress get() = args
}