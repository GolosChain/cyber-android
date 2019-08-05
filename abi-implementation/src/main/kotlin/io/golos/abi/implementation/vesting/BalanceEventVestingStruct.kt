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
data class BalanceEventVestingStruct(
  val account: CyberName,
  val vesting: CyberAsset,
  val delegated: CyberAsset,
  val received: CyberAsset
) {
  val structName: String = "balance_event"

  @ForTechUse
  val getAccount: CyberName
    @CyberNameCompress
    get() = account

  @ForTechUse
  val getVesting: CyberAsset
    @AssetCompress
    get() = vesting

  @ForTechUse
  val getDelegated: CyberAsset
    @AssetCompress
    get() = delegated

  @ForTechUse
  val getReceived: CyberAsset
    @AssetCompress
    get() = received

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishBalanceEventVestingStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
