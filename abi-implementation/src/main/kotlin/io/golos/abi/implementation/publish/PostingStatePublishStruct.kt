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
data class PostingStatePublishStruct(
  val id: Long,
  val max_vote_changes: StMaxVoteChangesPublishStruct,
  val cashout_window: StCashoutWindowPublishStruct,
  val max_beneficiaries: StMaxBeneficiariesPublishStruct,
  val max_comment_depth: StMaxCommentDepthPublishStruct,
  val social_acc: StSocialAccPublishStruct,
  val referral_acc: StReferralAccPublishStruct,
  val curators_prcnt: StCuratorsPrcntPublishStruct,
  val bwprovider: StBwproviderPublishStruct
) {
  val structName: String = "posting_state"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getMaxVoteChanges: StMaxVoteChangesPublishStruct
    @ChildCompress
    get() = max_vote_changes

  @ForTechUse
  val getCashoutWindow: StCashoutWindowPublishStruct
    @ChildCompress
    get() = cashout_window

  @ForTechUse
  val getMaxBeneficiaries: StMaxBeneficiariesPublishStruct
    @ChildCompress
    get() = max_beneficiaries

  @ForTechUse
  val getMaxCommentDepth: StMaxCommentDepthPublishStruct
    @ChildCompress
    get() = max_comment_depth

  @ForTechUse
  val getSocialAcc: StSocialAccPublishStruct
    @ChildCompress
    get() = social_acc

  @ForTechUse
  val getReferralAcc: StReferralAccPublishStruct
    @ChildCompress
    get() = referral_acc

  @ForTechUse
  val getCuratorsPrcnt: StCuratorsPrcntPublishStruct
    @ChildCompress
    get() = curators_prcnt

  @ForTechUse
  val getBwprovider: StBwproviderPublishStruct
    @ChildCompress
    get() = bwprovider

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishPostingStatePublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
