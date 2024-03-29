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
package io.golos.cyber4j.abi.writer.bytewriter

import io.golos.cyber4j.abi.writer.ByteWriter
import java.util.regex.Pattern

class AssetWriter(
    private val currencySymbolWriter: CurrencySymbolWriter = CurrencySymbolWriter()
) {

    fun put(asset: String, writer: ByteWriter) {

        val value = asset.trim()

        val pattern = Pattern.compile("^([0-9]+)\\.?([0-9]*)([ ][a-zA-Z0-9]{1,7})?$")
        val matcher = pattern.matcher(value)

        if (matcher.find()) {
            val beforeDotVal = matcher.group(1)
            val afterDotVal = matcher.group(2)

            val symbol = if (matcher.group(3).isEmpty()) null else matcher.group(3).trim()

            val amount = (beforeDotVal + afterDotVal).toLong()

            writer.putLong(amount)

            if (symbol != null) {
                currencySymbolWriter.put(afterDotVal.length, symbol, writer)
            } else {
                writer.putLong(0)
            }
        } else {
            throw IllegalArgumentException("invalid asset format")
        }
    }
}