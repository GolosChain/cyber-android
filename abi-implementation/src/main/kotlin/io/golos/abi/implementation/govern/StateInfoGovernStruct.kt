// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.govern

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.TimestampCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberTimeStampSeconds
import kotlin.Int
import kotlin.Long
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class StateInfoGovernStruct(
  val id: Long,
  val last_schedule_increase: CyberTimeStampSeconds,
  val block_num: Int,
  val target_emission_per_block: Long,
  val funds: Long,
  val last_propose_block_num: Int,
  val required_producers_num: Short,
  val last_producers_num: Short
) {
  val structName: String = "state_info"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getLastScheduleIncrease: CyberTimeStampSeconds
    @TimestampCompress
    get() = last_schedule_increase

  @ForTechUse
  val getBlockNum: Int
    @IntCompress
    get() = block_num

  @ForTechUse
  val getTargetEmissionPerBlock: Long
    @LongCompress
    get() = target_emission_per_block

  @ForTechUse
  val getFunds: Long
    @LongCompress
    get() = funds

  @ForTechUse
  val getLastProposeBlockNum: Int
    @IntCompress
    get() = last_propose_block_num

  @ForTechUse
  val getRequiredProducersNum: Short
    @ShortCompress
    get() = required_producers_num

  @ForTechUse
  val getLastProducersNum: Short
    @ShortCompress
    get() = last_producers_num

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStateInfoGovernStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
