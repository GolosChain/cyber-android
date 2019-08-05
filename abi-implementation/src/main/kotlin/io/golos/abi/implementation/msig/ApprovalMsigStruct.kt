// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberTimeStampMicroseconds
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class ApprovalMsigStruct(
  val level: PermissionLevelMsigStruct,
  val time: CyberTimeStampMicroseconds
) {
  val structName: String = "approval"

  @ForTechUse
  val getLevel: PermissionLevelMsigStruct
    @ChildCompress
    get() = level

  @ForTechUse
  val getTime: CyberTimeStampMicroseconds
    @LongCompress
    get() = time

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishApprovalMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
