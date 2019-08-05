// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.domain

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
data class DomainBidRefundDomainStruct(
  val bidder: CyberName,
  val amount: CyberAsset
) {
  val structName: String = "domain_bid_refund"

  @ForTechUse
  val getBidder: CyberName
    @CyberNameCompress
    get() = bidder

  @ForTechUse
  val getAmount: CyberAsset
    @AssetCompress
    get() = amount

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishDomainBidRefundDomainStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
