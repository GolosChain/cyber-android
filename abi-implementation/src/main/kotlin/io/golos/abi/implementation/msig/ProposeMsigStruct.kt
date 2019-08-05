// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
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
data class ProposeMsigStruct(
  val proposer: CyberName,
  val proposal_name: CyberName,
  val requested: List<PermissionLevelMsigStruct>,
  val trx: TransactionMsigStruct
) {
  val structName: String = "propose"

  @ForTechUse
  val getProposer: CyberName
    @CyberNameCompress
    get() = proposer

  @ForTechUse
  val getProposalName: CyberName
    @CyberNameCompress
    get() = proposal_name

  @ForTechUse
  val getRequested: List<PermissionLevelMsigStruct>
    @CollectionCompress
    get() = requested

  @ForTechUse
  val getTrx: TransactionMsigStruct
    @ChildCompress
    get() = trx

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishProposeMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
