// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

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
data class VestingStatsVestingStruct(
  val supply: CyberAsset,
  val notify_acc: CyberName
) {
  val structName: String = "vesting_stats"

  @ForTechUse
  val getSupply: CyberAsset
    @AssetCompress
    get() = supply

  @ForTechUse
  val getNotifyAcc: CyberName
    @CyberNameCompress
    get() = notify_acc

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVestingStatsVestingStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
