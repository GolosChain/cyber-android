// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.emit

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.IntCompress
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
data class InflationRateEmitStruct(
  val start: Short,
  val stop: Short,
  val narrowing: Int
) : EmitParamInterface {
  val structName: String = "inflation_rate"

  @ForTechUse
  val getStart: Short
    @ShortCompress
    get() = start

  @ForTechUse
  val getStop: Short
    @ShortCompress
    get() = stop

  @ForTechUse
  val getNarrowing: Int
    @IntCompress
    get() = narrowing

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 0
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishInflationRateEmitStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishInflationRateEmitStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
