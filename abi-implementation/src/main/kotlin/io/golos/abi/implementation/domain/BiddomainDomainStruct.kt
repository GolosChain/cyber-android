// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.domain

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
data class BiddomainDomainStruct(
  val bidder: CyberName,
  val name: String,
  val bid: CyberAsset
) {
  val structName: String = "biddomain"

  @ForTechUse
  val getBidder: CyberName
    @CyberNameCompress
    get() = bidder

  @ForTechUse
  val getName: String
    @StringCompress
    get() = name

  @ForTechUse
  val getBid: CyberAsset
    @AssetCompress
    get() = bid

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishBiddomainDomainStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
