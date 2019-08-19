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
import io.golos.cyber4j.chain.actions.transaction.AbiBinaryGenTransactionWriter
import io.golos.cyber4j.chain.actions.transaction.abi.ActionAbi
import io.golos.cyber4j.chain.actions.transaction.abi.TransactionAuthorizationAbi
import io.golos.cyber4j.chain.actions.transaction.account.actions.sellram.SellRamArgs
import io.golos.cyber4j.chain.actions.transaction.account.actions.sellram.SellRamBody
import io.golos.cyber4j.http.rpc.model.transaction.response.TransactionCommitted
import io.reactivex.Single
import java.util.Arrays.asList

class SellRamChain(chainApi: io.golos.cyber4j.http.rpc.ChainApi) : io.golos.cyber4j.chain.actions.transaction.ChainTransaction(chainApi) {

    data class Args(
            val quantity: Long
    )

    fun sellRam(
            args: Args,
            transactionContext: io.golos.cyber4j.chain.actions.transaction.TransactionContext
    ): Single<io.golos.cyber4j.chain.actions.ChainResponse<TransactionCommitted<Any>>> {

        return push(
                transactionContext.expirationDate,
                asList(ActionAbi(
                        "eosio",
                        "sellram",
                        asList(TransactionAuthorizationAbi(
                                transactionContext.authorizingAccountName,
                                "active")),
                        sellRamAbi(args, transactionContext)
                )),
                transactionContext.authorizingPrivateKey
        )
    }

    private fun sellRamAbi(args: Args, transactionContext: io.golos.cyber4j.chain.actions.transaction.TransactionContext): String {
        return AbiBinaryGenTransactionWriter(CompressionType.NONE).squishSellRamBody(
                SellRamBody(
                        SellRamArgs(
                                transactionContext.authorizingAccountName,
                                args.quantity)
                )
        ).toHex()
    }
}