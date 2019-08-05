// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.referral

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import io.golos.sharedmodel.CyberName
import kotlin.Long
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class AddreferralReferralStruct(
  val referrer: CyberName,
  val referral: CyberName,
  val percent: Short,
  val expire: Long,
  val breakout: CyberAsset
) {
  val structName: String = "addreferral"

  @ForTechUse
  val getReferrer: CyberName
    @CyberNameCompress
    get() = referrer

  @ForTechUse
  val getReferral: CyberName
    @CyberNameCompress
    get() = referral

  @ForTechUse
  val getPercent: Short
    @ShortCompress
    get() = percent

  @ForTechUse
  val getExpire: Long
    @LongCompress
    get() = expire

  @ForTechUse
  val getBreakout: CyberAsset
    @AssetCompress
    get() = breakout

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishAddreferralReferralStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
