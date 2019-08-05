package com.memtrip.eos.http.rpc

import com.memtrip.eos.http.rpc.model.ApiResponseError
import io.golos.sharedmodel.Either

interface SocketClient {

    fun <R> send(
            method: String,
            params: Any,
            classOfMessageToReceive: Class<R>
    ): Either<R, ApiResponseError>

    fun dropConnection()

}