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
package io.golos.cyber4j.chain.actions.transaction.account.actions.buyram

import io.golos.cyber4j.abi.writer.Abi
import io.golos.cyber4j.abi.writer.AccountNameCompress
import io.golos.cyber4j.abi.writer.AssetCompress

@Abi
data class BuyRamArgs(
    val payer: String,
    val receiver: String,
    val quant: String
) {

    val getCreator: String
        @AccountNameCompress get() = payer

    val getName: String
        @AccountNameCompress get() = receiver

    val getQuant: String
        @AssetCompress get() = quant
}