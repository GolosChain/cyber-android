// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.emit

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Int
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class RewardPoolsEmitStruct(
  val pools: List<RewardPoolEmitStruct>
) : EmitParamInterface {
  val structName: String = "reward_pools"

  @ForTechUse
  val getPools: List<RewardPoolEmitStruct>
    @CollectionCompress
    get() = pools

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 1
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishRewardPoolsEmitStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishRewardPoolsEmitStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
