// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.stake

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.SymbolCodeCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberSymbolCode
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class SetminstakedArgsStakeStruct(
  val account: CyberName,
  val token_code: CyberSymbolCode,
  val min_own_staked: Long
) {
  val structName: String = "setminstaked_args"

  @ForTechUse
  val getAccount: CyberName
    @CyberNameCompress
    get() = account

  @ForTechUse
  val getTokenCode: CyberSymbolCode
    @SymbolCodeCompress
    get() = token_code

  @ForTechUse
  val getMinOwnStaked: Long
    @LongCompress
    get() = min_own_staked

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishSetminstakedArgsStakeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
