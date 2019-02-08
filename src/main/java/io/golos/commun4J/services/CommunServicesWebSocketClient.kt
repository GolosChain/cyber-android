package io.golos.commun4J.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.services.model.Identifieble
import io.golos.commun4J.services.model.ServicesMessagesWrapper
import io.golos.commun4J.services.model.ServicesResponseWrapper
import io.golos.commun4J.utils.LogLevel
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


object CommunServicesWebSocketClient : WebSocketListener() {
    private lateinit var webSocket: WebSocket
    private lateinit var logLevel: LogLevel
    private val moshi = Moshi.Builder().build()
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
                .connectTimeout(config.connectionTimeOutInSeconds.toLong(), TimeUnit.SECONDS)
                .readTimeout(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .writeTimeout(config.writeTimeoutInSeconds.toLong(), TimeUnit.SECONDS)
                .build()

        logLevel = config.logLevel

        webSocket = client.newWebSocket(Request.Builder().url(config.servicesUrl).build(), this)
    }

    fun <R> send(method: String,
                 messageInJson: String,
                 classOfMessageToReceive: Class<R>): R {

        val rpcMessage = ServicesMessagesWrapper(method, messageInJson)
        webSocket.send(moshi.adapter(ServicesMessagesWrapper::class.java).toJson(rpcMessage))
        latches[rpcMessage.id] = CountDownLatch(1)
        latches[rpcMessage.id]!!.await()

        val response = responseMap[rpcMessage.id]!!

        val type = Types.newParameterizedType(ServicesResponseWrapper::class.java, classOfMessageToReceive)

        val responseWrapper = moshi.adapter<ServicesResponseWrapper<R>>(type).fromJson(response)

        return responseWrapper?.result?:throw IllegalStateException("cannot parse $response to $type")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (logLevel == LogLevel.BODY)
            println("on message $text")
        try {
            val response = moshi.adapter<Identifieble>(Identifieble::class.java).fromJson(text)!!
            responseMap[response.id] = text
            latches[response.id]?.countDown()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }
}