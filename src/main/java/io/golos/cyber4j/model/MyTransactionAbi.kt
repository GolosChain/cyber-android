package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.*

import java.util.*

@Abi
class MyTransactionAbi(
        val expiration: Date,
        val ref_block_num: Int,
        val ref_block_prefix: Long,
        val max_net_usage_words: Long,
        val max_cpu_usage_ms: Long,
        val delay_sec: Long,
        val context_free_actions: List<MyActionAbi>,
        val actions: List<MyActionAbi>,
        val transaction_extensions: List<String>,
        val signatures: List<String>,
        val context_free_data: List<String>
) {

    val getExpiration: Long
        @TimestampCompress get() = expiration.time

    val getRefBlockNum: Int
        @BlockNumCompress get() = ref_block_num

    val getRefBlockPrefix: Long
        @BlockPrefixCompress get() = ref_block_prefix

    val getMaxNetUsageWords: Long
        @VariableUIntCompress get() = max_net_usage_words

    val getMaxCpuUsageMs: Long
        @VariableUIntCompress get() = max_cpu_usage_ms

    val getDelaySec: Long
        @VariableUIntCompress get() = delay_sec

    val getContextFreeActions: List<MyActionAbi>
        @CollectionCompress get() = context_free_actions

    val getActions: List<MyActionAbi>
        @CollectionCompress get() = actions

    val getTransactionExtensions: List<String>
        @StringCollectionCompress get() = transaction_extensions
}