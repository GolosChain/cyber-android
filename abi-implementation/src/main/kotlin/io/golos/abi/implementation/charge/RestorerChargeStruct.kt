// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.charge

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.SymbolCodeCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberSymbolCode
import kotlin.Byte
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class RestorerChargeStruct(
  val charge_symbol: Long,
  val token_code: CyberSymbolCode,
  val charge_id: Byte,
  val func: BytecodeChargeStruct,
  val max_prev: Long,
  val max_vesting: Long,
  val max_elapsed: Long
) {
  val structName: String = "restorer"

  @ForTechUse
  val getChargeSymbol: Long
    @LongCompress
    get() = charge_symbol

  @ForTechUse
  val getTokenCode: CyberSymbolCode
    @SymbolCodeCompress
    get() = token_code

  @ForTechUse
  val getChargeId: Byte
    @ByteCompress
    get() = charge_id

  @ForTechUse
  val getFunc: BytecodeChargeStruct
    @ChildCompress
    get() = func

  @ForTechUse
  val getMaxPrev: Long
    @LongCompress
    get() = max_prev

  @ForTechUse
  val getMaxVesting: Long
    @LongCompress
    get() = max_vesting

  @ForTechUse
  val getMaxElapsed: Long
    @LongCompress
    get() = max_elapsed

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishRestorerChargeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
