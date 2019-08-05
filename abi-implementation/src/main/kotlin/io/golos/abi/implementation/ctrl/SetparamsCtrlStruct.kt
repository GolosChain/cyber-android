// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.ctrl

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.InterfaceCollectionCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class SetparamsCtrlStruct(
  val params: List<CtrlParamInterface>
) {
  val structName: String = "setparams"

  @ForTechUse
  val getParams: List<CtrlParamInterface>
    @InterfaceCollectionCompress
    get() = params

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishSetparamsCtrlStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
