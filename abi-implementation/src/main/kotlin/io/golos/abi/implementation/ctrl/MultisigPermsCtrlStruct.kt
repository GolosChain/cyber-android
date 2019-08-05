// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.ctrl

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Int
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class MultisigPermsCtrlStruct(
  val super_majority: Short,
  val majority: Short,
  val minority: Short
) : CtrlParamInterface {
  val structName: String = "multisig_perms"

  @ForTechUse
  val getSuperMajority: Short
    @ShortCompress
    get() = super_majority

  @ForTechUse
  val getMajority: Short
    @ShortCompress
    get() = majority

  @ForTechUse
  val getMinority: Short
    @ShortCompress
    get() = minority

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 3
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMultisigPermsCtrlStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMultisigPermsCtrlStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
