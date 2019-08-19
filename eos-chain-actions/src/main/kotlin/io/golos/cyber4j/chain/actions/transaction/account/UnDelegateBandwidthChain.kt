/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.golos.cyber4j.chain.actions.transaction.account

import io.golos.cyber4j.abi.writer.compression.CompressionType
import io.golos.cyber4j.chain.actions.ChainResponse
import io.golos.cyber4j.chain.actions.transaction.AbiBinaryGenTransactionWriter
import io.golos.cyber4j.chain.actions.transaction.ChainTransaction
import io.golos.cyber4j.chain.actions.transaction.TransactionContext
import io.golos.cyber4j.chain.actions.transaction.abi.ActionAbi
import io.golos.cyber4j.chain.actions.transaction.abi.TransactionAuthorizationAbi
import io.golos.cyber4j.chain.actions.transaction.account.actions.undelegatebw.UnDelegateBandwidthArgs
import io.golos.cyber4j.chain.actions.transaction.account.actions.undelegatebw.UnDelegateBandwidthBody
import io.golos.cyber4j.http.rpc.ChainApi
import io.golos.cyber4j.http.rpc.model.transaction.response.TransactionCommitted
import io.reactivex.Single
import java.util.Arrays.asList

class UnDelegateBandwidthChain(chainApi: io.golos.cyber4j.http.rpc.ChainApi) : io.golos.cyber4j.chain.actions.transaction.ChainTransaction(chainApi) {

    data class Args(
        val from: String,
        val receiver: String,
        val netQuantity: String,
        val cpuQuantity: String
    )

    fun unDelegateBandwidth(
        args: Args,
        transactionContext: io.golos.cyber4j.chain.actions.transaction.TransactionContext
    ): Single<io.golos.cyber4j.chain.actions.ChainResponse<TransactionCommitted<Any>>> {

        return push(
            transactionContext.expirationDate,
            asList(ActionAbi(
                "eosio",
                "undelegatebw",
                asList(TransactionAuthorizationAbi(
                    transactionContext.authorizingAccountName,
                    "active")),
                delegateBandwidthBin(args)
            )),
            transactionContext.authorizingPrivateKey
        )
    }

    private fun delegateBandwidthBin(args: Args): String {
        return AbiBinaryGenTransactionWriter(CompressionType.NONE).squishUnDelegateBandwidthBody(
            UnDelegateBandwidthBody(
                UnDelegateBandwidthArgs(
                    args.from,
                    args.receiver,
                    args.netQuantity,
                    args.cpuQuantity)
            )
        ).toHex()
    }
}