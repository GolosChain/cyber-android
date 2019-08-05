// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.emit

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Int
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class EmitIntervalEmitStruct(
  val value: Short
) : EmitParamInterface {
  val structName: String = "emit_interval"

  @ForTechUse
  val getValue: Short
    @ShortCompress
    get() = value

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 3
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishEmitIntervalEmitStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishEmitIntervalEmitStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
