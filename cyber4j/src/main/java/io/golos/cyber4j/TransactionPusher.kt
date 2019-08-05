package io.golos.cyber4j

import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import io.golos.cyber4j.model.BandWidthRequest
import io.golos.cyber4j.model.BandWidthSource
import io.golos.sharedmodel.Cyber4JConfig
import io.golos.sharedmodel.Either
import io.golos.sharedmodel.GolosEosError

internal class TransactionPusherImpl(private val cyber4JConfig: Cyber4JConfig) {


    fun <T : Any> pushTransaction(action: List<ActionAbi>,
                                  key: EosPrivateKey,
                                  traceType: Class<T>,
                                  bandWidthSource: BandWidthRequest? = null): Either<TransactionCommitted<T>, GolosEosError> {

        return com.memtrip.eos.chain.actions.transaction.TransactionPusher.pushTransaction(action, key,
                traceType, cyber4JConfig.blockChainHttpApiUrl,
                bandWidthSource?.source == BandWidthSource.USING_KEY,
                bandWidthSource?.key,
                cyber4JConfig.logLevel,
                cyber4JConfig.httpLogger)
    }
}
