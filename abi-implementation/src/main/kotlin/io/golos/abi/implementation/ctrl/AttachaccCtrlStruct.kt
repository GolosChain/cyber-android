// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.ctrl

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class AttachaccCtrlStruct(
  val user: CyberName
) {
  val structName: String = "attachacc"

  @ForTechUse
  val getUser: CyberName
    @CyberNameCompress
    get() = user

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishAttachaccCtrlStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
