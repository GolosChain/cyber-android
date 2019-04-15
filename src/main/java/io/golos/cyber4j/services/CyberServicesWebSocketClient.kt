package io.golos.cyber4j.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.services.model.*
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.LogLevel
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

interface ApiClient {

    fun <R> send(
        method: String,
        params: Any,
        classOfMessageToReceive: Class<R>
    ): Either<R, ApiResponseError>

    fun unAuth()

    fun setAuthRequestListener(listener: AuthRequestListener?)
}

interface AuthRequestListener {

    fun onAuthRequest(secret: String)
}


internal class CyberServicesWebSocketClient(
    private val config: Cyber4JConfig,
    private val moshi: Moshi
) : WebSocketListener(), ApiClient {
    private lateinit var webSocket: WebSocket
    private val latches = Collections.synchronizedMap<Long, CountDownLatch>(hashMapOf())
    private val responseMap = Collections.synchronizedMap<Long, String?>(hashMapOf())

    private var authListener: AuthRequestListener? = null


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

        if (config.logLevel == LogLevel.BODY) println("sending: $stringToSend")

        webSocket.send(stringToSend)

        val id = rpcMessage.id

        latches[id] = CountDownLatch(1)
        latches[id]!!.await(config.readTimeoutInSeconds.toLong(), TimeUnit.SECONDS)

        val response = responseMap[id]
            ?: throw SocketTimeoutException(
                "socket was unable to answer " +
                        "for request $stringToSend"
            )

        responseMap[id] = null
        latches[id] = null

        val type = Types.newParameterizedType(ServicesResponseWrapper::class.java, classOfMessageToReceive)

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
        latches.forEach(action =  {
            it.value?.countDown()
        })
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        System.err.println("socket is closing by $reason with code $code")
        super.onClosing(webSocket, code, reason)
        latches.forEach(action =  {
            it.value?.countDown()
        })
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (config.logLevel == LogLevel.BODY)
            println("on message $text")
        try {
            val id = moshi.adapter<Identifieble>(Identifieble::class.java).fromJson(text)?.id
            if (id != null) {
                responseMap[id] = text
                latches[id]?.countDown()
            } else if (text.contains("\"method\":\"sign\"")) {
                val type = Types.newParameterizedType(ServicesRequestWrapper::class.java, SecretRequest::class.java)
                val secret = moshi.adapter<ServicesRequestWrapper<SecretRequest>>(type).fromJson(text)
                    ?: return

                authListener?.onAuthRequest(secret.params.secret)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

    override fun setAuthRequestListener(listener: AuthRequestListener?) {
        this.authListener = listener
    }

    override fun unAuth() {
        synchronized(this) {
            if (::webSocket.isInitialized) webSocket.close(1000, "connection drop by user request")
        }
    }
}

private class SecretRequest(val secret: String)