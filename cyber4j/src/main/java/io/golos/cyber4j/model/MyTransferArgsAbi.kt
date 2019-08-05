package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal data class MyTransferArgsAbi(
        val from: String,
        val to: String,
        val quantity: String,
        val memo: String
) {

    val getFrom: String
        @NameCompress get() = from

    val getTo: String
        @NameCompress get() = to

    val getQuantity: String
        @AssetCompress get() = quantity

    val getMemo: String
        @StringCompress get() = memo
}