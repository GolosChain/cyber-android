// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.stake

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.SymbolCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberSymbol
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class RewardArgsStakeStruct(
  val rewards: List<NameIntPairStakeStruct>,
  val sym: CyberSymbol
) {
  val structName: String = "reward_args"

  @ForTechUse
  val getRewards: List<NameIntPairStakeStruct>
    @CollectionCompress
    get() = rewards

  @ForTechUse
  val getSym: CyberSymbol
    @SymbolCompress
    get() = sym

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishRewardArgsStakeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
