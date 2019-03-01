package io.golos.cyber4j

import com.memtrip.eos.http.rpc.Api
import com.memtrip.eos.http.rpc.ChainApi
import io.golos.cyber4j.utils.LogLevel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface ChainApiProvider {
    fun provide(): ChainApi
}

internal class GolosEosConfiguratedApi(config: io.golos.cyber4j.Cyber4JConfig = io.golos.cyber4j.Cyber4JConfig()) : io.golos.cyber4j.ChainApiProvider {

    private val api: Api

    init {

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(
                        when (config.logLevel) {
                            LogLevel.BASIC -> HttpLoggingInterceptor.Level.BASIC
                            LogLevel.BODY -> HttpLoggingInterceptor.Level.BODY
                            LogLevel.NONE -> HttpLoggingInterceptor.Level.NONE
                        }))
                .connectTimeout(config.connectionTimeOutInSeconds.toLong(), TimeUnit.SECONDS)
                .readTimeout(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .writeTimeout(config.writeTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .build()
        api = Api(config.blockChainHttpApiUrl, okHttpClient)
    }

    override fun provide(): ChainApi {
        return api.chain
    }
}