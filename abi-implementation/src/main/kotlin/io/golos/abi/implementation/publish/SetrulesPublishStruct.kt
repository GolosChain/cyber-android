// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.SymbolCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberSymbol
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class SetrulesPublishStruct(
  val mainfunc: FuncparamsPublishStruct,
  val curationfunc: FuncparamsPublishStruct,
  val timepenalty: FuncparamsPublishStruct,
  val maxtokenprop: Short,
  val tokensymbol: CyberSymbol
) {
  val structName: String = "setrules"

  @ForTechUse
  val getMainfunc: FuncparamsPublishStruct
    @ChildCompress
    get() = mainfunc

  @ForTechUse
  val getCurationfunc: FuncparamsPublishStruct
    @ChildCompress
    get() = curationfunc

  @ForTechUse
  val getTimepenalty: FuncparamsPublishStruct
    @ChildCompress
    get() = timepenalty

  @ForTechUse
  val getMaxtokenprop: Short
    @ShortCompress
    get() = maxtokenprop

  @ForTechUse
  val getTokensymbol: CyberSymbol
    @SymbolCompress
    get() = tokensymbol

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishSetrulesPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
