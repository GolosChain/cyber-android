package io.golos.cyber4j

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.core.block.BlockIdDetails
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.crypto.signature.PrivateKeySigning
import com.memtrip.eos.http.rpc.model.info.Info
import com.memtrip.eos.http.rpc.model.signing.PushTransaction
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.squareup.moshi.Types
import io.golos.cyber4j.model.*
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.LogLevel
import java.util.*

// helper class, used to split action creation and transaction push
interface TransactionPusher {
    fun <T> pushTransaction(action: List<MyActionAbi>,
                            key: EosPrivateKey,
                            traceType: Class<T>,
                            usingPrefetchedChainInfo: Info? = null): Either<TransactionSuccessful<T>, GolosEosError>
}

internal class TransactionPusherImpl(private val chainApi: CyberWayChainApi,
                                     private val cyber4JConfig: io.golos.cyber4j.Cyber4JConfig,
                                     private val moshi: Moshi) : TransactionPusher {


    override fun <T> pushTransaction(action: List<MyActionAbi>,
                                     key: EosPrivateKey,
                                     traceType: Class<T>,
                                     usingPrefetchedChainInfo: Info?): Either<TransactionSuccessful<T>, GolosEosError> {

        val info = usingPrefetchedChainInfo ?: chainApi.getInfo().blockingGet().body()!!

        val sdf = Calendar.getInstance(TimeZone.getTimeZone(cyber4JConfig.blockChainTimeZoneId))

        val serverDate = Date(sdf.timeInMillis + 30_000)

        val transaction = transaction(serverDate, BlockIdDetails(info.head_block_id), action)

        val signedTransaction = MySignedTransactionAbi(info.chain_id, transaction, emptyList())

        if (cyber4JConfig.logLevel == LogLevel.BODY) cyber4JConfig.logger?.log("signed transaction = ${Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build().adapter<MySignedTransactionAbi>(MySignedTransactionAbi::class.java).toJson(signedTransaction)}")

        val signature = PrivateKeySigning()
                .sign(
                        AbiBinaryGenCyber4J(CompressionType.NONE).squishMySignedTransactionAbi(
                                signedTransaction
                        ).toBytes(),
                        key
                )


        val result = chainApi.pushTransaction(
                PushTransaction(
                        listOf(signature),
                        "none",
                        "",
                        AbiBinaryGenCyber4J(CompressionType.NONE).squishMyTransactionAbi(transaction).toHex()
                )
        ).blockingGet()


        return if (result.isSuccessful) {

            val response = result.body()!!

            val type = Types.newParameterizedType(TransactionSuccessful::class.java, traceType)
            val jsonAdapter = moshi.adapter<TransactionSuccessful<T>>(type)

            return try {
                val value = jsonAdapter.fromJson(response)!!

                Either.Success(value)
            } catch (e: Exception) {
                Either.Failure(moshi.adapter<io.golos.cyber4j.model.GolosEosError>(io.golos.cyber4j.model.GolosEosError::class.java).fromJson(response)!!)
            }

        } else {
            Either.Failure(moshi.adapter<io.golos.cyber4j.model.GolosEosError>(io.golos.cyber4j.model.GolosEosError::class.java).fromJson(result.errorBody()?.string().orEmpty())!!)
        }
    }

    private fun transaction(
            expirationDate: Date,
            blockIdDetails: BlockIdDetails,
            actions: List<MyActionAbi>
    ): MyTransactionAbi {
        return MyTransactionAbi(
                expirationDate,
                blockIdDetails.blockNum,
                blockIdDetails.blockPrefix,
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
