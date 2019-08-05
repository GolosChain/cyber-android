// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.SymbolCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberSymbol
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class OpenVestingStruct(
  val owner: CyberName,
  val symbol: CyberSymbol,
  val ram_payer: CyberName
) {
  val structName: String = "open"

  @ForTechUse
  val getOwner: CyberName
    @CyberNameCompress
    get() = owner

  @ForTechUse
  val getSymbol: CyberSymbol
    @SymbolCompress
    get() = symbol

  @ForTechUse
  val getRamPayer: CyberName
    @CyberNameCompress
    get() = ram_payer

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishOpenVestingStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
