// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CheckSumCompress
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CheckSum256
import io.golos.sharedmodel.CyberName
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class ApproveMsigStruct(
  val proposer: CyberName,
  val proposal_name: CyberName,
  val level: PermissionLevelMsigStruct,
  val proposal_hash: CheckSum256
) {
  val structName: String = "approve"

  @ForTechUse
  val getProposer: CyberName
    @CyberNameCompress
    get() = proposer

  @ForTechUse
  val getProposalName: CyberName
    @CyberNameCompress
    get() = proposal_name

  @ForTechUse
  val getLevel: PermissionLevelMsigStruct
    @ChildCompress
    get() = level

  @ForTechUse
  val getProposalHash: CheckSum256
    @CheckSumCompress
    get() = proposal_hash

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishApproveMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
