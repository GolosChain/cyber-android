// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.ctrl

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BoolCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.StringCompress
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
data class WitnessInfoCtrlStruct(
  val name: CyberName,
  val url: String,
  val active: Boolean,
  val total_weight: Long,
  val counter_votes: Long
) {
  val structName: String = "witness_info"

  @ForTechUse
  val getName: CyberName
    @CyberNameCompress
    get() = name

  @ForTechUse
  val getUrl: String
    @StringCompress
    get() = url

  @ForTechUse
  val getActive: Boolean
    @BoolCompress
    get() = active

  @ForTechUse
  val getTotalWeight: Long
    @LongCompress
    get() = total_weight

  @ForTechUse
  val getCounterVotes: Long
    @LongCompress
    get() = counter_votes

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishWitnessInfoCtrlStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
