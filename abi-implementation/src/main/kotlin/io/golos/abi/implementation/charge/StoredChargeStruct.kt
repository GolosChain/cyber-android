// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.charge

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import java.math.BigInteger
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class StoredChargeStruct(
  val id: Long,
  val symbol_stamp: BigInteger,
  val value: Long
) {
  val structName: String = "stored"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getSymbolStamp: ByteArray
    @BytesCompress
    get() = ByteArray(16) { 0 }.also { System.arraycopy(symbol_stamp.toByteArray(), 0, it, 0,
        symbol_stamp.toByteArray().size) }.reversedArray()

  @ForTechUse
  val getValue: Long
    @LongCompress
    get() = value

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStoredChargeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
