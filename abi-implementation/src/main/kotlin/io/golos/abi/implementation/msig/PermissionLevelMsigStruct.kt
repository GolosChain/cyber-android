// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

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
data class PermissionLevelMsigStruct(
  val actor: CyberName,
  val permission: CyberName
) {
  val structName: String = "permission_level"

  @ForTechUse
  val getActor: CyberName
    @CyberNameCompress
    get() = actor

  @ForTechUse
  val getPermission: CyberName
    @CyberNameCompress
    get() = permission

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishPermissionLevelMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
