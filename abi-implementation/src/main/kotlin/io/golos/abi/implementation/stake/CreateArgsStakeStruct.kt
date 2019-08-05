// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.stake

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.SymbolCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberSymbol
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class CreateArgsStakeStruct(
  val token_symbol: CyberSymbol,
  val max_proxies: ByteArray,
  val depriving_window: Long,
  val min_own_staked_for_election: Long
) {
  val structName: String = "create_args"

  @ForTechUse
  val getTokenSymbol: CyberSymbol
    @SymbolCompress
    get() = token_symbol

  @ForTechUse
  val getMaxProxies: ByteArray
    @BytesCompress
    get() = max_proxies

  @ForTechUse
  val getDeprivingWindow: Long
    @LongCompress
    get() = depriving_window

  @ForTechUse
  val getMinOwnStakedForElection: Long
    @LongCompress
    get() = min_own_staked_for_election

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishCreateArgsStakeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
