// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.charge

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.SymbolCodeCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberSymbolCode
import kotlin.Byte
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class RemovestoredChargeStruct(
  val user: CyberName,
  val token_code: CyberSymbolCode,
  val charge_id: Byte,
  val stamp_id: Long
) {
  val structName: String = "removestored"

  @ForTechUse
  val getUser: CyberName
    @CyberNameCompress
    get() = user

  @ForTechUse
  val getTokenCode: CyberSymbolCode
    @SymbolCodeCompress
    get() = token_code

  @ForTechUse
  val getChargeId: Byte
    @ByteCompress
    get() = charge_id

  @ForTechUse
  val getStampId: Long
    @LongCompress
    get() = stamp_id

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishRemovestoredChargeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
