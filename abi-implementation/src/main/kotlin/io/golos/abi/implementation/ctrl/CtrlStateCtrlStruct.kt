// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.ctrl

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class CtrlStateCtrlStruct(
  val id: Long,
  val token: CtrlTokenCtrlStruct,
  val multisig: MultisigAccCtrlStruct,
  val witnesses: MaxWitnessesCtrlStruct,
  val msig_perms: MultisigPermsCtrlStruct,
  val witness_votes: MaxWitnessVotesCtrlStruct,
  val update_auth_period: UpdateAuthCtrlStruct
) {
  val structName: String = "ctrl_state"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getToken: CtrlTokenCtrlStruct
    @ChildCompress
    get() = token

  @ForTechUse
  val getMultisig: MultisigAccCtrlStruct
    @ChildCompress
    get() = multisig

  @ForTechUse
  val getWitnesses: MaxWitnessesCtrlStruct
    @ChildCompress
    get() = witnesses

  @ForTechUse
  val getMsigPerms: MultisigPermsCtrlStruct
    @ChildCompress
    get() = msig_perms

  @ForTechUse
  val getWitnessVotes: MaxWitnessVotesCtrlStruct
    @ChildCompress
    get() = witness_votes

  @ForTechUse
  val getUpdateAuthPeriod: UpdateAuthCtrlStruct
    @ChildCompress
    get() = update_auth_period

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishCtrlStateCtrlStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
