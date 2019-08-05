// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.token

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.StringCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import io.golos.sharedmodel.CyberName
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class RecipientTokenStruct(
  val to: CyberName,
  val quantity: CyberAsset,
  val memo: String
) {
  val structName: String = "recipient"

  @ForTechUse
  val getTo: CyberName
    @CyberNameCompress
    get() = to

  @ForTechUse
  val getQuantity: CyberAsset
    @AssetCompress
    get() = quantity

  @ForTechUse
  val getMemo: String
    @StringCompress
    get() = memo

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishRecipientTokenStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
