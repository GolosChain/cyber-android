package io.golos.cyber4j.model


data class TransactionSuccessful<T>(
        val transaction_id: String,
        val processed: TransactionProcessed<T>
)


