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
package com.memtrip.eos.abi.writer

import com.memtrip.eos.core.crypto.EosPublicKey
import io.golos.sharedmodel.CyberAsset
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberSymbol
import io.golos.sharedmodel.CyberSymbolCode

interface ByteWriter {
    fun putName(value: String)
    fun putName(value: CyberName)
    fun putAccountName(value: String)
    fun putBlockNum(value: Int)
    fun putBlockPrefix(value: Long)
    fun putPublicKey(value: EosPublicKey)
    fun putAsset(value: String)
    fun putAsset(value: CyberAsset)
    fun putChainId(value: String)
    fun putData(value: String)
    fun putTimestampMs(value: Long)
    fun putBoolean(value: Boolean)

    fun putShort(value: Short)
    fun putNullableShort(value: Short?)

    fun putInt(value: Int)
    fun putVariableUInt(value: Long)
    fun putLong(value: Long)
    fun putFloat(value: Float)
    fun putBytes(value: ByteArray)

    fun putString(value: String)
    fun putNullableString(value: String?)

    fun putByte(value: Byte)
    fun putStringCollection(stringList: List<String>)
    fun putLongCollection(longsList: List<Long>)
    fun putHexCollection(stringList: List<String>)
    fun putAccountNameCollection(accountNameList: List<String>)

    fun putSymbolCode(value: CyberSymbolCode)
    fun putSymbol(value: CyberSymbol)

    fun toBytes(): ByteArray
    fun length(): Int
}