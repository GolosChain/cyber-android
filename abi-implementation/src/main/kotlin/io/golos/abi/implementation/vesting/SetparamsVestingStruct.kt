// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.InterfaceCollectionCompress
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
data class SetparamsVestingStruct(
  val symbol: CyberSymbol,
  val params: List<VestingParamInterface>
) {
  val structName: String = "setparams"

  @ForTechUse
  val getSymbol: CyberSymbol
    @SymbolCompress
    get() = symbol

  @ForTechUse
  val getParams: List<VestingParamInterface>
    @InterfaceCollectionCompress
    get() = params

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishSetparamsVestingStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
