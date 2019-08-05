// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.stake

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
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
data class DelegateArgsStakeStruct(
  val grantor_name: CyberName,
  val recipient_name: CyberName,
  val quantity: CyberAsset
) {
  val structName: String = "delegate_args"

  @ForTechUse
  val getGrantorName: CyberName
    @CyberNameCompress
    get() = grantor_name

  @ForTechUse
  val getRecipientName: CyberName
    @CyberNameCompress
    get() = recipient_name

  @ForTechUse
  val getQuantity: CyberAsset
    @AssetCompress
    get() = quantity

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishDelegateArgsStakeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
