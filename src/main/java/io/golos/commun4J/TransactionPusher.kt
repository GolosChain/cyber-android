package io.golos.commun4J

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.core.block.BlockIdDetails
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.crypto.signature.PrivateKeySigning
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.signing.PushTransaction
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.golos.commun4J.model.MyActionAbi
import io.golos.commun4J.model.MySignedTransactionAbi
import io.golos.commun4J.model.MyTransactionAbi
import io.golos.commun4J.model.TransactionSuccessful
import java.text.SimpleDateFormat
import java.util.*

interface TransactionPusher {
    fun pushTransaction(action: List<MyActionAbi>,
                        key: EosPrivateKey): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError>
}

class GolosEosTransactionPusher(private val chainApi: ChainApi,
                                private val commun4JConfig: io.golos.commun4J.Commun4JConfig,
                                private val moshi: Moshi) : TransactionPusher {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat(commun4JConfig.datePattern).apply {
        timeZone = TimeZone.getTimeZone(commun4JConfig.timeZoneId)
    }


    override fun pushTransaction(action: List<MyActionAbi>,
                                 key: EosPrivateKey): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {

        val info = chainApi.getInfo().blockingGet().body()!!

        val serverDate = Date(dateFormat.parse(info.head_block_time).time + 30_000)

        val transaction = transaction(serverDate, BlockIdDetails(info.head_block_id), action)

        val signedTransaction = MySignedTransactionAbi(info.chain_id, transaction, emptyList())

        print("signed transaction = ${Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build().adapter<MySignedTransactionAbi>(MySignedTransactionAbi::class.java).toJson(signedTransaction)}")

        val signature = PrivateKeySigning()
                .sign(
                        io.golos.commun4J.model.AbiBinaryGenRomo(CompressionType.NONE).squishMySignedTransactionAbi(
                                signedTransaction
                        ).toBytes(),
                        key
                )


        val result = chainApi.pushTransaction(
                PushTransaction(
                        listOf(signature),
                        "none",
                        "",
                        io.golos.commun4J.model.AbiBinaryGenRomo(CompressionType.NONE).squishMyTransactionAbi(transaction).toHex()
                )
        ).blockingGet()
        return if (result.isSuccessful) {
            val comitted = result.body()!!
            io.golos.commun4J.Either.Success(TransactionSuccessful(comitted))
        } else {
            io.golos.commun4J.Either.Failure(moshi.adapter<io.golos.commun4J.model.GolosEosError>(io.golos.commun4J.model.GolosEosError::class.java).fromJson(result.errorBody()?.string().orEmpty())!!)
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
