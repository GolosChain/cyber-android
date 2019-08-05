// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BoolCompress
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.NullableAssetCompress
import com.memtrip.eos.abi.writer.NullableShortCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.StringCollectionCompress
import com.memtrip.eos.abi.writer.StringCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import kotlin.Boolean
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class CreatemssgPublishStruct(
  val message_id: MssgidPublishStruct,
  val parent_id: MssgidPublishStruct,
  val beneficiaries: List<BeneficiaryPublishStruct>,
  val tokenprop: Short,
  val vestpayment: Boolean,
  val headermssg: String,
  val bodymssg: String,
  val languagemssg: String,
  val tags: List<String>,
  val jsonmetadata: String,
  val curators_prcnt: Short?,
  val max_payout: CyberAsset?
) {
  val structName: String = "createmssg"

  @ForTechUse
  val getMessageId: MssgidPublishStruct
    @ChildCompress
    get() = message_id

  @ForTechUse
  val getParentId: MssgidPublishStruct
    @ChildCompress
    get() = parent_id

  @ForTechUse
  val getBeneficiaries: List<BeneficiaryPublishStruct>
    @CollectionCompress
    get() = beneficiaries

  @ForTechUse
  val getTokenprop: Short
    @ShortCompress
    get() = tokenprop

  @ForTechUse
  val getVestpayment: Boolean
    @BoolCompress
    get() = vestpayment

  @ForTechUse
  val getHeadermssg: String
    @StringCompress
    get() = headermssg

  @ForTechUse
  val getBodymssg: String
    @StringCompress
    get() = bodymssg

  @ForTechUse
  val getLanguagemssg: String
    @StringCompress
    get() = languagemssg

  @ForTechUse
  val getTags: List<String>
    @StringCollectionCompress
    get() = tags

  @ForTechUse
  val getJsonmetadata: String
    @StringCompress
    get() = jsonmetadata

  @ForTechUse
  val getCuratorsPrcnt: Short?
    @NullableShortCompress
    get() = curators_prcnt

  @ForTechUse
  val getMaxPayout: CyberAsset?
    @NullableAssetCompress
    get() = max_payout

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishCreatemssgPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
