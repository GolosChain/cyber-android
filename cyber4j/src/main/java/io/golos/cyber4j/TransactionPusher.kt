package io.golos.cyber4j

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.AbiBinaryGenTransactionWriter
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.SignedTransactionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAbi
import com.memtrip.eos.core.block.BlockIdDetails
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.crypto.signature.PrivateKeySigning
import com.memtrip.eos.http.rpc.model.signing.PushTransaction
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.golos.cyber4j.model.CyberWayChainApi
import io.golos.sharedmodel.Cyber4JConfig
import io.golos.sharedmodel.Either
import io.golos.sharedmodel.GolosEosError
import io.golos.sharedmodel.LogLevel
import java.util.*

// helper class, used to split action creation and transaction push
interface TransactionPusher {
    fun <T> pushTransaction(action: List<ActionAbi>,
                            key: EosPrivateKey,
                            traceType: Class<T>): Either<TransactionCommitted<T>, GolosEosError>
}

internal class TransactionPusherImpl(private val chainApi: CyberWayChainApi,
                                     private val cyber4JConfig: Cyber4JConfig,
                                     private val moshi: Moshi) : TransactionPusher {


    override fun <T> pushTransaction(action: List<ActionAbi>,
                                     key: EosPrivateKey,
                                     traceType: Class<T>): Either<TransactionCommitted<T>, GolosEosError> {

        val info = chainApi.getInfo().blockingGet().body()!!

        val sdf = Calendar.getInstance(TimeZone.getTimeZone(cyber4JConfig.blockChainTimeZoneId))

        val serverDate = Date(sdf.timeInMillis + 30_000)

        val transaction = transaction(serverDate, BlockIdDetails(info.head_block_id), action)

        val signedTransaction = SignedTransactionAbi(info.chain_id, transaction, emptyList())

        if (cyber4JConfig.logLevel == LogLevel.BODY)
            cyber4JConfig.httpLogger?.log("signed transaction = ${moshi
                    .adapter<SignedTransactionAbi>(SignedTransactionAbi::class.java).toJson(signedTransaction)}")

        val signature = PrivateKeySigning()
                .sign(
                        AbiBinaryGenTransactionWriter(CompressionType.NONE)
                                .squishSignedTransactionAbi(signedTransaction).toBytes(),
                        key)


        val result = chainApi.pushTransaction(
                PushTransaction(
                        listOf(signature),
                        "none",
                        "",
                        AbiBinaryGenTransactionWriter(CompressionType.NONE).squishTransactionAbi(transaction).toHex()
                )
        ).blockingGet()


        return if (result.isSuccessful) {

            val response = result.body()!!

            val type = Types.newParameterizedType(TransactionCommitted::class.java, traceType)
            val jsonAdapter = moshi.adapter<TransactionCommitted<T>>(type)

            return try {
                val value = jsonAdapter.nullSafe().fromJson(response)!!

                Either.Success(value.copy(
                        resolvedResponse = value.processed.action_traces.map {
                            moshi.adapter<T>(traceType)
                                    .fromJsonValue(it.act.data)
                        }.firstOrNull()))
            } catch (e: Exception) {
                val err = moshi.adapter<GolosEosError>(GolosEosError::class.java).fromJson(response)
                if (err == null) {
                    e.printStackTrace()
                    throw e
                }
                Either.Failure(err)
            }
        } else {
            Either.Failure(moshi
                    .adapter<GolosEosError>(GolosEosError::class.java)
                    .fromJson(result.errorBody()?.string().orEmpty())!!)
        }
    }

    private fun transaction(
            expirationDate: Date,
            blockIdDetails: BlockIdDetails,
            actions: List<ActionAbi>
    ): TransactionAbi {
        return TransactionAbi(
                expirationDate,
                blockIdDetails.blockNum,
                blockIdDetails.blockPrefix,
                0,
                0,
                0,
                0,
                0,
                emptyList(),
                actions,
                emptyList(),
                emptyList(),
                emptyList()
        )
    }
}
