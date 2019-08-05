// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

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
data class FuncinfoPublishStruct(
  val code: BytecodePublishStruct,
  val maxarg: Long
) {
  val structName: String = "funcinfo"

  @ForTechUse
  val getCode: BytecodePublishStruct
    @ChildCompress
    get() = code

  @ForTechUse
  val getMaxarg: Long
    @LongCompress
    get() = maxarg

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishFuncinfoPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
