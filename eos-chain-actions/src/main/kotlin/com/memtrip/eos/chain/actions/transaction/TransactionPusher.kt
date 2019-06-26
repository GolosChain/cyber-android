package com.memtrip.eos.chain.actions.transaction

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.SignedTransactionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAbi
import com.memtrip.eos.core.block.BlockIdDetails
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.crypto.signature.PrivateKeySigning
import com.memtrip.eos.http.rpc.model.info.Info
import com.memtrip.eos.http.rpc.model.signing.PushTransaction
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.golos.sharedmodel.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import java.util.concurrent.Executors

object TransactionPusher {

    private val moshi: Moshi = Moshi
            .Builder()
            .add(CyberName::class.java, CyberNameAdapter())
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .add(CyberAsset::class.java, CyberAssetAdapter())
            .add(CyberSymbol::class.java, CyberSymbolAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

    private var okHttpClient = OkHttpClient
            .Builder()
            .dispatcher(Dispatcher(Executors.newSingleThreadExecutor { Thread.currentThread() }))
            .build()

    private var lastConfig: Cyber4JConfig? = null


    @JvmOverloads
    fun <T> pushTransaction(action: List<ActionAbi>,
                            key: EosPrivateKey,
                            traceType: Class<T>,
                            withConfig: Cyber4JConfig = Cyber4JConfig.default): Either<TransactionCommitted<T>, GolosEosError> {

        if (lastConfig != withConfig) {
            okHttpClient = okHttpClient.newBuilder().also {
                it.interceptors().also {
                    it.clear()
                    if (withConfig.httpLogger != null)
                        it.add(HttpLoggingInterceptor(withConfig.httpLogger!!))
                }
            }.build()
            lastConfig = withConfig
        }

        val resp = okHttpClient.newCall(Request.Builder()
                .post(RequestBody.create(MediaType.parse("application/json"), ""))
                .url("${withConfig.blockChainHttpApiUrl}v1/chain/get_info")
                .build())
                .execute()


        if (!resp.isSuccessful) {
            return Either.Failure(moshi.adapter<GolosEosError>(GolosEosError::class.java).fromJson(resp.body()?.string()
                    ?: resp.cacheResponse()?.body()!!.string())!!)
        }

        val info = moshi.adapter<Info>(Info::class.java).fromJson(resp.body()!!.string())!!

        val sdf = Calendar.getInstance(TimeZone.getTimeZone(withConfig.blockChainTimeZoneId))

        val serverDate = Date(sdf.timeInMillis + 30_000)

        val transaction = transaction(serverDate, BlockIdDetails(info.head_block_id), action)

        val signedTransaction = SignedTransactionAbi(info.chain_id, transaction, emptyList())

        if (withConfig.logLevel == LogLevel.BODY) withConfig.httpLogger?.log("signed transaction = ${moshi
                .adapter<SignedTransactionAbi>(SignedTransactionAbi::class.java).toJson(signedTransaction)}")

        val signature = PrivateKeySigning()
                .sign(AbiBinaryGenTransactionWriter(CompressionType.NONE)
                        .squishSignedTransactionAbi(signedTransaction)
                        .toBytes(), key)

        val result = okHttpClient.newCall(Request.Builder()
                .post(RequestBody.create(MediaType.parse("application/json"),
                        moshi.adapter<PushTransaction>(PushTransaction::class.java).toJson(PushTransaction
                        (
                                listOf(signature), "none", "",
                                AbiBinaryGenTransactionWriter(CompressionType.NONE).squishTransactionAbi(transaction).toHex()
                        ))))
                .url("${withConfig.blockChainHttpApiUrl}v1/chain/push_transaction")
                .build())
                .execute()


        return if (result.isSuccessful) {
            val response = result.body()!!.string()

            return try {

                val type = Types.newParameterizedType(TransactionCommitted::class.java, traceType)
                val value = moshi
                        .adapter<TransactionCommitted<T>>(type)
                        .fromJson(response)!!

                Either.Success(value.copy(
                        resolvedResponse = value.processed.action_traces.map {
                            moshi.adapter<T>(traceType).fromJsonValue(it.act.data)
                        }.firstOrNull()
                ))
            } catch (e: Exception) {
                e.printStackTrace()
                Either.Failure(moshi.adapter<GolosEosError>(GolosEosError::class.java).fromJson(response)!!)
            }
        } else {
            Either.Failure(moshi.adapter<GolosEosError>(GolosEosError::class.java).fromJson(result.body()!!.string().orEmpty())!!)
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