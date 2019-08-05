// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import io.golos.sharedmodel.CyberName
import kotlin.Long
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class MessagePublishStruct(
  val author: CyberName,
  val id: Long,
  val date: Long,
  val pool_date: Long,
  val tokenprop: Short,
  val beneficiaries: List<BeneficiaryPublishStruct>,
  val rewardweight: Short,
  val state: MessagestatePublishStruct,
  val curators_prcnt: Short,
  val cashout_time: Long,
  val mssg_reward: CyberAsset,
  val max_payout: CyberAsset,
  val paid_amount: Long
) {
  val structName: String = "message"

  @ForTechUse
  val getAuthor: CyberName
    @CyberNameCompress
    get() = author

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getDate: Long
    @LongCompress
    get() = date

  @ForTechUse
  val getPoolDate: Long
    @LongCompress
    get() = pool_date

  @ForTechUse
  val getTokenprop: Short
    @ShortCompress
    get() = tokenprop

  @ForTechUse
  val getBeneficiaries: List<BeneficiaryPublishStruct>
    @CollectionCompress
    get() = beneficiaries

  @ForTechUse
  val getRewardweight: Short
    @ShortCompress
    get() = rewardweight

  @ForTechUse
  val getState: MessagestatePublishStruct
    @ChildCompress
    get() = state

  @ForTechUse
  val getCuratorsPrcnt: Short
    @ShortCompress
    get() = curators_prcnt

  @ForTechUse
  val getCashoutTime: Long
    @LongCompress
    get() = cashout_time

  @ForTechUse
  val getMssgReward: CyberAsset
    @AssetCompress
    get() = mssg_reward

  @ForTechUse
  val getMaxPayout: CyberAsset
    @AssetCompress
    get() = max_payout

  @ForTechUse
  val getPaidAmount: Long
    @LongCompress
    get() = paid_amount

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMessagePublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
