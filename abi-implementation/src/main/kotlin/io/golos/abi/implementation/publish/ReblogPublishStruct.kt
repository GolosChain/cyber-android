// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.StringCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class ReblogPublishStruct(
  val rebloger: CyberName,
  val message_id: MssgidPublishStruct,
  val headermssg: String,
  val bodymssg: String
) {
  val structName: String = "reblog"

  @ForTechUse
  val getRebloger: CyberName
    @CyberNameCompress
    get() = rebloger

  @ForTechUse
  val getMessageId: MssgidPublishStruct
    @ChildCompress
    get() = message_id

  @ForTechUse
  val getHeadermssg: String
    @StringCompress
    get() = headermssg

  @ForTechUse
  val getBodymssg: String
    @StringCompress
    get() = bodymssg

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishReblogPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
