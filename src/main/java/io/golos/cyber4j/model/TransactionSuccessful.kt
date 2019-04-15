package io.golos.cyber4j.model

import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted


data class TransactionSuccessful<T>(
        val transaction_id: String,

        val processed: TransactionProcessed<T>
) {
    constructor(comitted: TransactionCommitted) : this(comitted.transaction_id, TransactionProcessed(comitted.processed))
}


data class TransactionProcessed<T>(
        val id: String,
        val receipt: TransactionParentReceipt,
        val elapsed: Int,
        val net_usage: Int,
        val scheduled: Boolean,
        val action_traces: List<TransactionActionTrace<T>>,
        val except: Any?
) {
    constructor(proceede: com.memtrip.eos.http.rpc.model.transaction.response.TransactionProcessed) :
            this(proceede.id, TransactionParentReceipt(proceede.receipt), proceede.elapsed, proceede.net_usage,
                    proceede.scheduled, proceede.action_traces.map { TransactionActionTrace<T>(it) }, proceede.except)
}


data class TransactionParentReceipt(
        val status: String,
        val cpu_usage_us: Int,
        val net_usage_words: Int
) {
    constructor(receipt: com.memtrip.eos.http.rpc.model.transaction.response.TransactionParentReceipt) : this(receipt.status, receipt.cpu_usage_us, receipt.net_usage_words)
}


data class TransactionActionTrace<T>(
        val receipt: TransactionReceipt,
        val act: TransactionAct<T>,
        val elapsed: Int,
        val cpu_usage: Int = -1,
        val console: String,
        val total_cpu_usage: Int = -1,
        val trx_id: String,
        val inline_traces: List<TransactionActionTrace<T>>
) {
    constructor(trace: com.memtrip.eos.http.rpc.model.transaction.response.TransactionActionTrace) : this(TransactionReceipt(trace.receipt),
            TransactionAct(trace.act), trace.elapsed, trace.cpu_usage, trace.console, trace.total_cpu_usage, trace.trx_id, trace.inline_traces.map {
        TransactionActionTrace<T>(it)
    })
}


data class TransactionReceipt(
        val receiver: String,
        val act_digest: String,
        val global_sequence: Long,
        val recv_sequence: Long,
        val auth_sequence: List<Any>,
        val code_sequence: Long,
        val abi_sequence: Long
) {
    constructor(receipt: com.memtrip.eos.http.rpc.model.transaction.response.TransactionReceipt) : this(receipt.receiver,
            receipt.act_digest, receipt.global_sequence, receipt.recv_sequence, receipt.auth_sequence, receipt.code_sequence,
            receipt.abi_sequence)
}


data class TransactionAct<T>(
        val account: String,
        val name: String,
        val authorization: List<MyTransactionAuthorizationAbi>,
        val data: T,
        val hex_data: String?
) {
    constructor(act: com.memtrip.eos.http.rpc.model.transaction.response.TransactionAct) : this(act.account,
            act.name, act.authorization.map { MyTransactionAuthorizationAbi(it) }, act.data as T, act.hex_data)
}