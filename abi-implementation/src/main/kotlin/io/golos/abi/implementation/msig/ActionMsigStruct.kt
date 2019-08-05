// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.ByteArray
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class ActionMsigStruct(
  val account: CyberName,
  val name: CyberName,
  val authorization: List<PermissionLevelMsigStruct>,
  val data: ByteArray
) {
  val structName: String = "action"

  @ForTechUse
  val getAccount: CyberName
    @CyberNameCompress
    get() = account

  @ForTechUse
  val getName: CyberName
    @CyberNameCompress
    get() = name

  @ForTechUse
  val getAuthorization: List<PermissionLevelMsigStruct>
    @CollectionCompress
    get() = authorization

  @ForTechUse
  val getData: ByteArray
    @BytesCompress
    get() = data

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishActionMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
