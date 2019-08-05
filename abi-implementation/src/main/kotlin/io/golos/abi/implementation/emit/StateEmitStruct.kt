// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.emit

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BoolCompress
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import java.math.BigInteger
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class StateEmitStruct(
  val id: Long,
  val prev_emit: Long,
  val tx_id: BigInteger,
  val start_time: Long,
  val active: Boolean
) {
  val structName: String = "state"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getPrevEmit: Long
    @LongCompress
    get() = prev_emit

  @ForTechUse
  val getTxId: ByteArray
    @BytesCompress
    get() = ByteArray(16) { 0 }.also { System.arraycopy(tx_id.toByteArray(), 0, it, 0,
        tx_id.toByteArray().size) }.reversedArray()

  @ForTechUse
  val getStartTime: Long
    @LongCompress
    get() = start_time

  @ForTechUse
  val getActive: Boolean
    @BoolCompress
    get() = active

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStateEmitStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
