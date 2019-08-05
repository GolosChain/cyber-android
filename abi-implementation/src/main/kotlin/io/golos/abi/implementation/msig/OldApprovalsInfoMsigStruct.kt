// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CollectionCompress
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
data class OldApprovalsInfoMsigStruct(
  val proposal_name: CyberName,
  val requested_approvals: List<PermissionLevelMsigStruct>,
  val provided_approvals: List<PermissionLevelMsigStruct>
) {
  val structName: String = "old_approvals_info"

  @ForTechUse
  val getProposalName: CyberName
    @CyberNameCompress
    get() = proposal_name

  @ForTechUse
  val getRequestedApprovals: List<PermissionLevelMsigStruct>
    @CollectionCompress
    get() = requested_approvals

  @ForTechUse
  val getProvidedApprovals: List<PermissionLevelMsigStruct>
    @CollectionCompress
    get() = provided_approvals

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishOldApprovalsInfoMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
