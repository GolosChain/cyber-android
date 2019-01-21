package io.golos.commun4J

import com.memtrip.eos.http.rpc.Api
import com.memtrip.eos.http.rpc.ChainApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface ChainApiProvider {
    fun provide(): ChainApi
}

class GolosEosConfiguratedApi(config: io.golos.commun4J.Commun4JConfig = io.golos.commun4J.Commun4JConfig(),
                              logLevel: io.golos.commun4J.GolosEosConfiguratedApi.LogLevel = io.golos.commun4J.GolosEosConfiguratedApi.LogLevel.BASIC) : io.golos.commun4J.ChainApiProvider {

    enum class LogLevel { NONE, BASIC, BODY }

    private val api: Api

    init {

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(
                        when (logLevel) {
                            io.golos.commun4J.GolosEosConfiguratedApi.LogLevel.BASIC -> HttpLoggingInterceptor.Level.BASIC
                            io.golos.commun4J.GolosEosConfiguratedApi.LogLevel.BODY -> HttpLoggingInterceptor.Level.BODY
                            io.golos.commun4J.GolosEosConfiguratedApi.LogLevel.NONE -> HttpLoggingInterceptor.Level.NONE
                        }))
                .connectTimeout(config.connectionTimeOutInSeconds.toLong(), TimeUnit.SECONDS)
                .readTimeout(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .writeTimeout(config.writeTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .build()
        api = Api(config.connectionUrl, okHttpClient)

    }

    override fun provide(): ChainApi {
        return api.chain
    }
}