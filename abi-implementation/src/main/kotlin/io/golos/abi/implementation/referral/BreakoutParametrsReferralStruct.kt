// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.referral

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import kotlin.Int
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class BreakoutParametrsReferralStruct(
  val min_breakout: CyberAsset,
  val max_breakout: CyberAsset
) : ReferralParamInterface {
  val structName: String = "breakout_parametrs"

  @ForTechUse
  val getMinBreakout: CyberAsset
    @AssetCompress
    get() = min_breakout

  @ForTechUse
  val getMaxBreakout: CyberAsset
    @AssetCompress
    get() = max_breakout

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 0
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishBreakoutParametrsReferralStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishBreakoutParametrsReferralStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
