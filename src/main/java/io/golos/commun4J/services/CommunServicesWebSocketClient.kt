package io.golos.commun4J.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.squareup.moshi.Types
import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.services.model.ApiResponseError
import io.golos.commun4J.services.model.Identifieble
import io.golos.commun4J.services.model.ServicesMessagesWrapper
import io.golos.commun4J.services.model.ServicesResponseWrapper
import io.golos.commun4J.utils.BigIntegerAdapter
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.LogLevel
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.math.BigInteger
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

interface ApiClient {

    fun <R> send(method: String,
                 params: Any,
                 classOfMessageToReceive: Class<R>,
                 isList: Boolean = false): Either<R, ApiResponseError>
}


internal class CommunServicesWebSocketClient : WebSocketListener(), ApiClient {
    private lateinit var webSocket: WebSocket
    private lateinit var config: Commun4JConfig
    private val moshi =
            Moshi.Builder()
                    .add(Date::class.java, Rfc3339DateJsonAdapter())
                    .add(BigInteger::class.java, BigIntegerAdapter())
                    .build()
    private val latches = Collections.synchronizedMap<Long, CountDownLatch>(hashMapOf())
    private val responseMap = Collections.synchronizedMap<Long, String>(hashMapOf())


    fun connect(config: Commun4JConfig = io.golos.commun4J.Commun4JConfig()) {

        synchronized(this) {
            if (::webSocket.isInitialized) return
        }


        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(
                when (config.logLevel) {
                    LogLevel.BASIC -> HttpLoggingInterceptor.Level.BASIC
                    LogLevel.BODY -> HttpLoggingInterceptor.Level.BODY
                    LogLevel.NONE -> HttpLoggingInterceptor.Level.NONE
                }))
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .writeTimeout(config.writeTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .build()

        this.config = config
        webSocket = client.newWebSocket(Request.Builder().url(config.servicesUrl).build(), this)
    }

    override fun <R> send(method: String,
                          params: Any,
                          classOfMessageToReceive: Class<R>,
                          isList: Boolean): Either<R, ApiResponseError> {

        val rpcMessage = ServicesMessagesWrapper(method, params)

        val stringToSend = moshi.adapter(ServicesMessagesWrapper::class.java).toJson(rpcMessage)

        if (config.logLevel == LogLevel.BODY) println("sending: $stringToSend")

        webSocket.send(stringToSend)

        val id = rpcMessage.id

        latches[id] = CountDownLatch(1)
        latches[id]!!.await(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)

        val response = responseMap[id]
                ?: throw SocketTimeoutException("socket was unable to answer " +
                        "for request $stringToSend")

        responseMap[id] = ""
        latches[id] = null

        val type = if (!isList)
            Types.newParameterizedType(ServicesResponseWrapper::class.java, classOfMessageToReceive)
        else Types.newParameterizedType(ServicesResponseWrapper::class.java, List::class.java, classOfMessageToReceive)

        val responseWrapper = moshi.adapter<ServicesResponseWrapper<R>>(type).fromJson(response)

        if (responseWrapper?.result == null) {
            val error = moshi.adapter<ApiResponseError>(ApiResponseError::class.java).fromJson(response)
            if (error != null) return Either.Failure(error)
            else throw IllegalStateException("cannot parse $response")
        }
        return Either.Success(responseWrapper.result)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        System.err.println("socket failure $response")
        t.printStackTrace()
        latches.forEach { _, u -> u.countDown() }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        System.err.println("socket is closing by $reason with code $code")
        super.onClosing(webSocket, code, reason)
        latches.forEach { _, u -> u.countDown() }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (config.logLevel == LogLevel.BODY)
            println("on message $text")
        try {
            val id = moshi.adapter<Identifieble>(Identifieble::class.java).fromJson(text)?.id
                    ?: return
            responseMap[id] = text
            latches[id]?.countDown()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }
}