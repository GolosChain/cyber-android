// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.domain

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.StringCompress
import com.memtrip.eos.abi.writer.TimestampCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberTimeStampSeconds
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class DomainBidDomainStruct(
  val id: Long,
  val name: String,
  val high_bidder: CyberName,
  val high_bid: Long,
  val last_bid_time: CyberTimeStampSeconds
) {
  val structName: String = "domain_bid"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getName: String
    @StringCompress
    get() = name

  @ForTechUse
  val getHighBidder: CyberName
    @CyberNameCompress
    get() = high_bidder

  @ForTechUse
  val getHighBid: Long
    @LongCompress
    get() = high_bid

  @ForTechUse
  val getLastBidTime: CyberTimeStampSeconds
    @TimestampCompress
    get() = last_bid_time

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishDomainBidDomainStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
