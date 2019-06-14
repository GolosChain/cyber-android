package io.golos.cyber4j.model


data class TransactionSuccessful<T>(
        val transaction_id: String,

        val processed: TransactionProcessed<T>
)


data class TransactionProcessed<T>(
        val id: String,
        val receipt: TransactionParentReceipt,
        val block_num: Long,
        val elapsed: Int,
        val net_usage: Int,
        val scheduled: Boolean,
        val action_traces: List<TransactionActionTrace<T>>,
        val except: Any?
)


data class TransactionParentReceipt(
        val status: String,
        val cpu_usage_us: Int,
        val net_usage_words: Int
)


data class TransactionActionTrace<T>(
        val receipt: TransactionReceipt,
        val act: TransactionAct<T>,
        val elapsed: Int,
        val cpu_usage: Int = -1,
        val console: String,
        val total_cpu_usage: Int = -1,
        val trx_id: String,
        val inline_traces: List<TransactionActionTrace<T>>
)


data class TransactionReceipt(
        val receiver: String,
        val act_digest: String,
        val global_sequence: Long,
        val recv_sequence: Long,
        val auth_sequence: List<Any>,
        val code_sequence: Long,
        val abi_sequence: Long
)

data class TransactionAct<T>(
        val account: String,
        val name: String,
        val authorization: List<MyTransactionAuthorizationAbi>,
        val data: T,
        val hex_data: String?
)