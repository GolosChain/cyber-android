package io.golos.cyber4j.http.rpc

import io.golos.cyber4j.http.rpc.model.ApiResponseError
import io.golos.cyber4j.sharedmodel.Either

interface SocketClient {

    fun <R> send(
            method: String,
            params: Any,
            classOfMessageToReceive: Class<R>
    ): Either<R, ApiResponseError>

    fun dropConnection()

}