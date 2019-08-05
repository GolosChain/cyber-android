// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.emit

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class EmitStateEmitStruct(
  val id: Long,
  val infrate: InflationRateEmitStruct,
  val pools: RewardPoolsEmitStruct,
  val token: EmitTokenEmitStruct,
  val interval: EmitIntervalEmitStruct,
  val bwprovider: BwproviderEmitStruct
) {
  val structName: String = "emit_state"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getInfrate: InflationRateEmitStruct
    @ChildCompress
    get() = infrate

  @ForTechUse
  val getPools: RewardPoolsEmitStruct
    @ChildCompress
    get() = pools

  @ForTechUse
  val getToken: EmitTokenEmitStruct
    @ChildCompress
    get() = token

  @ForTechUse
  val getInterval: EmitIntervalEmitStruct
    @ChildCompress
    get() = interval

  @ForTechUse
  val getBwprovider: BwproviderEmitStruct
    @ChildCompress
    get() = bwprovider

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishEmitStateEmitStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
