package io.golos.cyber4j

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.core.block.BlockIdDetails
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.crypto.signature.PrivateKeySigning
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.info.Info
import com.memtrip.eos.http.rpc.model.signing.PushTransaction
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.squareup.moshi.Types
import io.golos.cyber4j.model.*
import io.golos.cyber4j.utils.Either
import java.text.SimpleDateFormat
import java.util.*

// helper class, used to split action creation and transaction push
interface TransactionPusher {
    fun <T> pushTransaction(action: List<MyActionAbi>,
                            key: EosPrivateKey,
                            traceType: Class<T>,
                            usingPrefetchedChainInfo: Info? = null): Either<TransactionSuccessful<T>, GolosEosError>
}

internal class TransactionPusherImpl(private val chainApi: ChainApi,
                                     private val cyber4JConfig: io.golos.cyber4j.Cyber4JConfig,
                                     private val moshi: Moshi) : TransactionPusher {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat(cyber4JConfig.datePattern).apply {
        timeZone = TimeZone.getTimeZone(cyber4JConfig.blockChainTimeZoneId)
    }


    override fun <T> pushTransaction(action: List<MyActionAbi>,
                                     key: EosPrivateKey,
                                     traceType: Class<T>,
                                     usingPrefetchedChainInfo: Info?): Either<TransactionSuccessful<T>, GolosEosError> {

        val info = usingPrefetchedChainInfo ?: chainApi.getInfo().blockingGet().body()!!

        val serverDate = Date(dateFormat.parse(info.head_block_time).time + 30_000)

        val transaction = transaction(serverDate, BlockIdDetails(info.head_block_id), action)

        val signedTransaction = MySignedTransactionAbi(info.chain_id, transaction, emptyList())

        println("signed transaction = ${Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build().adapter<MySignedTransactionAbi>(MySignedTransactionAbi::class.java).toJson(signedTransaction)}")

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
            val responseString = moshi.adapter<TransactionCommitted>(TransactionCommitted::class.java).toJson(response)

            val type = Types.newParameterizedType(TransactionSuccessful::class.java, traceType)
            val jsonAdapter = moshi.adapter<TransactionSuccessful<T>>(type)
            val value = jsonAdapter.fromJson(responseString)!!

            return Either.Success(value)
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