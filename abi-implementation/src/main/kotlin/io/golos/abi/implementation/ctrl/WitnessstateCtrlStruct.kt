// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.ctrl

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BoolCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class WitnessstateCtrlStruct(
  val witness: CyberName,
  val weight: Long,
  val active: Boolean
) {
  val structName: String = "witnessstate"

  @ForTechUse
  val getWitness: CyberName
    @CyberNameCompress
    get() = witness

  @ForTechUse
  val getWeight: Long
    @LongCompress
    get() = weight

  @ForTechUse
  val getActive: Boolean
    @BoolCompress
    get() = active

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishWitnessstateCtrlStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
