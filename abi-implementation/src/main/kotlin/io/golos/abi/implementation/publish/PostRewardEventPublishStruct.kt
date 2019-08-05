// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class PostRewardEventPublishStruct(
  val message_id: MssgidPublishStruct,
  val author_reward: CyberAsset,
  val benefactor_reward: CyberAsset,
  val curator_reward: CyberAsset,
  val unclaimed_reward: CyberAsset
) {
  val structName: String = "post_reward_event"

  @ForTechUse
  val getMessageId: MssgidPublishStruct
    @ChildCompress
    get() = message_id

  @ForTechUse
  val getAuthorReward: CyberAsset
    @AssetCompress
    get() = author_reward

  @ForTechUse
  val getBenefactorReward: CyberAsset
    @AssetCompress
    get() = benefactor_reward

  @ForTechUse
  val getCuratorReward: CyberAsset
    @AssetCompress
    get() = curator_reward

  @ForTechUse
  val getUnclaimedReward: CyberAsset
    @AssetCompress
    get() = unclaimed_reward

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishPostRewardEventPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
