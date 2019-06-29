package io.golos.cyber4j.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.golos.cyber4j.GolosEosConfiguratedApi
import io.golos.cyber4j.services.model.ApiResponseError
import io.golos.cyber4j.services.model.Identifieble
import io.golos.cyber4j.services.model.ServicesMessagesWrapper
import io.golos.cyber4j.services.model.ServicesResponseWrapper
import io.golos.sharedmodel.Cyber4JConfig
import io.golos.sharedmodel.Either
import io.golos.sharedmodel.LogLevel
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

interface ApiClient {

    fun <R> send(
            method: String,
            params: Any,
            classOfMessageToReceive: Class<R>
    ): Either<R, ApiResponseError>

    fun unAuth()

}

internal class CyberServicesWebSocketClient(
        private val config: Cyber4JConfig,
        private val moshi: Moshi
) : WebSocketListener(), ApiClient {
    private lateinit var webSocket: WebSocket
    private val latches = Collections.synchronizedMap<Long, CountDownLatch>(hashMapOf())
    private val responseMap = Collections.synchronizedMap<Long, String?>(hashMapOf())

    private fun connect() {

        synchronized(this) {
            if (::webSocket.isInitialized) return
        }

        val client = OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().setLevel(
                        when (config.logLevel) {
                            LogLevel.BASIC -> HttpLoggingInterceptor.Level.BASIC
                            LogLevel.BODY -> HttpLoggingInterceptor.Level.BODY
                            LogLevel.NONE -> HttpLoggingInterceptor.Level.NONE
                        }
                )
        )
                .connectTimeout(config.connectionTimeOutInSeconds.toLong(), TimeUnit.SECONDS)
                .readTimeout(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .connectionPool(GolosEosConfiguratedApi.connectionPool)
                .writeTimeout(config.writeTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .build()

        webSocket = client.newWebSocket(Request.Builder().url(config.servicesUrl).build(), this)
    }

    override fun <R> send(
            method: String,
            params: Any,
            classOfMessageToReceive: Class<R>
    ): Either<R, ApiResponseError> {

        connect()

        val rpcMessage = ServicesMessagesWrapper(method, params)

        val stringToSend = moshi.adapter(ServicesMessagesWrapper::class.java).toJson(rpcMessage)

        log("sending $stringToSend")
        log("Thread ${Thread.currentThread()}")
        webSocket.send(stringToSend)

        val id = rpcMessage.id

        latches[id] = CountDownLatch(1)
        latches[id]!!.await(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)

        val response = responseMap[id]
                ?: throw SocketTimeoutException(
                        "socket was unable to answer " +
                                "for request $stringToSend"
                )
        println("response")
        responseMap[id] = null
        latches[id] = null

        val type = Types.newParameterizedType(ServicesResponseWrapper::class.java, classOfMessageToReceive)

        val responseWrapper = moshi.adapter<ServicesResponseWrapper<R>>(type).fromJson(response)

        if (responseWrapper?.result == null) {
            val error = moshi.adapter(ApiResponseError::class.java).fromJson(response)
            if (error != null) return Either.Failure(error)
            else throw IllegalStateException("cannot parse $response")
        }
        return Either.Success(responseWrapper.result)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        System.err.println("socket failure $response")
        t.printStackTrace()
        latches.forEach(action = {
            it.value?.countDown()
        })
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        System.err.println("socket is closing by $reason with code $code")
        super.onClosing(webSocket, code, reason)
        latches.forEach(action = {
            it.value?.countDown()
        })
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        log("onMessage $text")
        log("Thread ${Thread.currentThread()}")
        try {
            val id = moshi.adapter(Identifieble::class.java).nullSafe().fromJson(text)?.id
            if (id != null) {
                responseMap[id] = text
                latches[id]?.countDown()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }


    override fun unAuth() {
        synchronized(this) {
            if (::webSocket.isInitialized) webSocket.close(1000, "connection drop by user request")
        }
    }

    private fun log(str: String) {
        if (config.logLevel != LogLevel.BODY) return
        config.socketLogger?.log(str)
        if (config.socketLogger == null) println(str)
    }
}