// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

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
data class RewardpoolPublishStruct(
  val created: Long,
  val rules: RewardrulesPublishStruct,
  val state: PoolstatePublishStruct
) {
  val structName: String = "rewardpool"

  @ForTechUse
  val getCreated: Long
    @LongCompress
    get() = created

  @ForTechUse
  val getRules: RewardrulesPublishStruct
    @ChildCompress
    get() = rules

  @ForTechUse
  val getState: PoolstatePublishStruct
    @ChildCompress
    get() = state

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishRewardpoolPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
