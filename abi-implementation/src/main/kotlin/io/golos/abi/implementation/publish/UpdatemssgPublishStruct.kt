// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.StringCollectionCompress
import com.memtrip.eos.abi.writer.StringCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class UpdatemssgPublishStruct(
  val message_id: MssgidPublishStruct,
  val headermssg: String,
  val bodymssg: String,
  val languagemssg: String,
  val tags: List<String>,
  val jsonmetadata: String
) {
  val structName: String = "updatemssg"

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

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishUpdatemssgPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
