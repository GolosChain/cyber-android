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
import io.golos.cyber4j.core.crypto.EosPublicKey
import io.golos.cyber4j.core.hex.DefaultHexWriter
import io.golos.cyber4j.core.hex.HexWriter
import io.golos.cyber4j.sharedmodel.*
import io.golos.cyber4j.sharedmodel.*

class DefaultByteWriter(
        capacity: Int
) : ByteWriter {

    private val nameWriter: NameWriter = NameWriter()
    private val accountNameWriter: AccountNameWriter = AccountNameWriter()
    private val publicKeyWriter: PublicKeyWriter = PublicKeyWriter()
    private val hexWriter: HexWriter = DefaultHexWriter()
    private val assetWriter: AssetWriter = AssetWriter()
    private val chainIdWriter: ChainIdWriter = ChainIdWriter()
    private val hexCollectionWriter: HexCollectionWriter = HexCollectionWriter()

    private val buffer = ByteArrayBuffer(capacity)

    override fun putName(value: String) {
        nameWriter.put(value, this)
    }

    override fun putName(value: CyberName) {
        nameWriter.put(value.name, this)
    }

    override fun putNullableName(value: CyberName?) {
        putNullable(value) { name ->
            putName(name)
        }
    }

    override fun putAccountName(value: String) {
        accountNameWriter.put(value, this)
    }

    override fun putBlockNum(value: Int) {
        putShort((value and 0xFFFF).toShort())
    }

    override fun putBlockPrefix(value: Long) {
        putInt((value and -0x1).toInt())
    }

    override fun putPublicKey(value: EosPublicKey) {
        publicKeyWriter.put(value, this)
    }

    override fun putPublicKey(value: String) {
        putPublicKey(EosPublicKey(value))
    }

    override fun putAsset(value: String) {
        assetWriter.put(value, this)
    }

    override fun putAsset(value: CyberAsset) {
        assetWriter.put(value.amount, this)
    }

    override fun putNullableAsset(value: CyberAsset?) {
        putNullable(value) { asset ->
            putAsset(asset)
        }
    }

    override fun putSymbolCode(value: CyberSymbolCode) {
        putString(value.value)
    }

    override fun putSymbol(value: CyberSymbol) {
        putBytes(value.symbolCode)
    }

    override fun putChainId(value: String) {
        chainIdWriter.put(value, this)
    }

    override fun putData(value: String) {
        val dataAsBytes = hexWriter.hexToBytes(value)
        putVariableUInt(dataAsBytes.size.toLong())
        putBytes(dataAsBytes)
    }

    override fun putCheckSum(value: CheckSum256) {
        putBytes(value.value)
    }

    override fun putTimestampMs(value: Long) {
        putInt((value / 1000).toInt())
    }

    override fun putTimestampMs(value: CyberTimeStampSeconds) {
        putTimestampMs(value.value)
    }

    override fun putBoolean(value: Boolean) {
        buffer.append(if (value) (1).toByte() else (0).toByte())
    }

    override fun putShort(value: Short) {
        buffer.append(value)
    }

    override fun putNullableShort(value: Short?) {
        putNullable(value) { short ->
            putShort(short)
        }
    }

    override fun putInt(value: Int) {
        buffer.append(value)
    }

    override fun putVariableUInt(value: Long) {
        var v: Long = value
        while (v >= 0x80) {
            val b = ((v and 0x7f) or 0x80).toByte()
            buffer.append(b)
            v = v ushr 7
        }
        buffer.append(v.toByte())
    }

    override fun putVariableUInt(value: Varuint) {
        putVariableUInt(value.value)
    }

    override fun putLong(value: Long) {
        buffer.append(value)
    }

    override fun putLong(value: CyberTimeStampMicroseconds) {
        putLong(value.value)
    }

    override fun putFloat(value: Float) {
        buffer.append(value)
    }

    override fun putBytes(value: ByteArray) {
        buffer.append(value)
    }

    override fun putByte(value: Byte) {
        buffer.append(value)
    }

    override fun putString(value: String) {
        val bytes = value.toByteArray()
        putVariableUInt(bytes.size.toLong())
        buffer.append(value.toByteArray())
    }

    override fun putNullableString(value: String?) {
        putNullable(value) { string -> putString(string) }
    }

    override fun putLongCollection(longsList: List<Long>) {
        putVariableUInt(longsList.size.toLong())

        if (longsList.isNotEmpty()) {
            for (string in longsList) {
                putLong(string)
            }
        }
    }

    override fun putStringCollection(stringList: List<String>) {
        putVariableUInt(stringList.size.toLong())

        if (stringList.isNotEmpty()) {
            for (string in stringList) {
                putString(string)
            }
        }
    }

    override fun putHexCollection(stringList: List<String>) {
        hexCollectionWriter.put(stringList, this)
    }

    override fun putAccountNameCollection(accountNameList: List<String>) {
        putVariableUInt(accountNameList.size.toLong())

        if (accountNameList.isNotEmpty()) {
            for (accountName in accountNameList) {
                putAccountName(accountName)
            }
        }
    }

    override fun putInterfaceCollection(collection: List<ISquishable>) {
        putVariableUInt(collection.size.toLong())
        collection.forEach {
            putVariableUInt(it.getStructIndexForCollectionSquish().toLong())
            putBytes(it.squish())
        }
    }

    override fun putCyberNamesCollection(accountNameList: List<CyberName>) {
        putAccountNameCollection(accountNameList.map { it.name })
    }

    override fun toBytes(): ByteArray = buffer.toByteArray()

    override fun length(): Int = buffer.length()

    private fun <T> putNullable(value: T?,
                                notNullAction: ByteWriter.(value: T) -> Unit) {
        putBoolean(value != null)
        if (value != null) this.notNullAction(value)
    }
}