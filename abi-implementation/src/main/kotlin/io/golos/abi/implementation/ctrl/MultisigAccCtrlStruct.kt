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
import kotlin.Int
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class MultisigAccCtrlStruct(
  val name: CyberName
) : CtrlParamInterface {
  val structName: String = "multisig_acc"

  @ForTechUse
  val getName: CyberName
    @CyberNameCompress
    get() = name

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 1
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMultisigAccCtrlStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMultisigAccCtrlStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
