package io.golos.cyber4j

import io.golos.cyber4j.chain.actions.transaction.TransactionPusher
import io.golos.cyber4j.chain.actions.transaction.abi.ActionAbi
import io.golos.cyber4j.core.crypto.EosPrivateKey
import io.golos.cyber4j.http.rpc.model.transaction.response.TransactionCommitted
import io.golos.cyber4j.model.BandWidthRequest
import io.golos.cyber4j.model.BandWidthSource
import io.golos.cyber4j.sharedmodel.Cyber4JConfig
import io.golos.cyber4j.sharedmodel.Either
import io.golos.cyber4j.sharedmodel.GolosEosError

internal class TransactionPusherImpl(private val cyber4JConfig: Cyber4JConfig) {


    fun <T : Any> pushTransaction(action: List<ActionAbi>,
                                  key: EosPrivateKey,
                                  traceType: Class<T>,
                                  bandWidthSource: BandWidthRequest? = null): Either<TransactionCommitted<T>, GolosEosError> {

        return TransactionPusher.pushTransaction(action, key,
                traceType, cyber4JConfig.blockChainHttpApiUrl,
                bandWidthSource?.source == BandWidthSource.USING_KEY,
                bandWidthSource?.key,
                cyber4JConfig.logLevel,
                cyber4JConfig.httpLogger)
    }
}
