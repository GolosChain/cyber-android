package io.golos.cyber4j.http.rpc.utils.testabi

import io.golos.cyber4j.abi.writer.Abi
import io.golos.cyber4j.abi.writer.ChildCompress

@Abi
data class TransferBody(
    val args: TransferArgs
) {

    val getArgs: TransferArgs
        @ChildCompress get() = args
}