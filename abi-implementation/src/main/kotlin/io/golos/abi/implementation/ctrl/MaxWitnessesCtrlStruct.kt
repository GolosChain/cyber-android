// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.ctrl

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
data class MaxWitnessesCtrlStruct(
  val max: Short
) : CtrlParamInterface {
  val structName: String = "max_witnesses"

  @ForTechUse
  val getMax: Short
    @ShortCompress
    get() = max

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 2
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMaxWitnessesCtrlStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMaxWitnessesCtrlStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
