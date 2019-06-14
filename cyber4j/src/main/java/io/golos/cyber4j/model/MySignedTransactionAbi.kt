package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChainIdCompress
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.HexCollectionCompress


@Abi
data class MySignedTransactionAbi(
        val chainId: String,
        val transaction: MyTransactionAbi,
        val context_free_data: List<String>
) {
    val getChainId: String
        @ChainIdCompress get() = chainId

    val getTransaction: MyTransactionAbi
        @ChildCompress get() = transaction

    val getContextFreeData: List<String>
        @HexCollectionCompress get() = context_free_data
}