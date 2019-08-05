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
data class SetmaxpayoutPublishStruct(
  val message_id: MssgidPublishStruct,
  val max_payout: CyberAsset
) {
  val structName: String = "setmaxpayout"

  @ForTechUse
  val getMessageId: MssgidPublishStruct
    @ChildCompress
    get() = message_id

  @ForTechUse
  val getMaxPayout: CyberAsset
    @AssetCompress
    get() = max_payout

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishSetmaxpayoutPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
