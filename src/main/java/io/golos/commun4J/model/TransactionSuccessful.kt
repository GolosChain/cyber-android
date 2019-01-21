package io.golos.commun4J.model

import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionSuccessful(
        val transaction_id: String,
        val processed: TransactionProcessed
) {
    constructor(comitted: TransactionCommitted) : this(comitted.transaction_id, TransactionProcessed(comitted.processed))
}

@JsonClass(generateAdapter = true)
data class TransactionProcessed(
        val id: String,
        val receipt: TransactionParentReceipt,
        val elapsed: Int,
        val net_usage: Int,
        val scheduled: Boolean,
        val action_traces: List<TransactionActionTrace>,
        val except: Any?
) {
    constructor(proceede: com.memtrip.eos.http.rpc.model.transaction.response.TransactionProcessed) :
            this(proceede.id, TransactionParentReceipt(proceede.receipt), proceede.elapsed, proceede.net_usage,
                    proceede.scheduled, proceede.action_traces.map { TransactionActionTrace(it) }, proceede.except)
}

@JsonClass(generateAdapter = true)
data class TransactionParentReceipt(
        val status: String,
        val cpu_usage_us: Int,
        val net_usage_words: Int
) {
    constructor(receipt: com.memtrip.eos.http.rpc.model.transaction.response.TransactionParentReceipt) : this(receipt.status, receipt.cpu_usage_us, receipt.net_usage_words)
}

@JsonClass(generateAdapter = true)
data class TransactionActionTrace(
        val receipt: TransactionReceipt,
        val act: TransactionAct,
        val elapsed: Int,
        val cpu_usage: Int = -1,
        val console: String,
        val total_cpu_usage: Int = -1,
        val trx_id: String,
        val inline_traces: List<TransactionActionTrace>
) {
    constructor(trace: com.memtrip.eos.http.rpc.model.transaction.response.TransactionActionTrace) : this(TransactionReceipt(trace.receipt),
            TransactionAct(trace.act), trace.elapsed, trace.cpu_usage, trace.console, trace.total_cpu_usage, trace.trx_id, trace.inline_traces.map {
        TransactionActionTrace(it)
    })
}

@JsonClass(generateAdapter = true)
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

@JsonClass(generateAdapter = true)
data class TransactionAct(
        val account: String,
        val name: String,
        val authorization: List<MyTransactionAuthorizationAbi>,
        val data: Any,
        val hex_data: String?
) {
    constructor(act: com.memtrip.eos.http.rpc.model.transaction.response.TransactionAct) : this(act.account,
            act.name, act.authorization.map { MyTransactionAuthorizationAbi(it) }, act.data, act.hex_data)
}