// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.LongCollectionCompress
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
data class BytecodePublishStruct(
  val varssize: Long,
  val operators: List<Long>,
  val values: List<ValuePublishStruct>,
  val nums: List<Long>,
  val consts: List<Long>
) {
  val structName: String = "bytecode"

  @ForTechUse
  val getVarssize: Long
    @LongCompress
    get() = varssize

  @ForTechUse
  val getOperators: List<Long>
    @LongCollectionCompress
    get() = operators

  @ForTechUse
  val getValues: List<ValuePublishStruct>
    @CollectionCompress
    get() = values

  @ForTechUse
  val getNums: List<Long>
    @LongCollectionCompress
    get() = nums

  @ForTechUse
  val getConsts: List<Long>
    @LongCollectionCompress
    get() = consts

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishBytecodePublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
