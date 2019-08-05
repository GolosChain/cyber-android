// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class RewardrulesPublishStruct(
  val mainfunc: FuncinfoPublishStruct,
  val curationfunc: FuncinfoPublishStruct,
  val timepenalty: FuncinfoPublishStruct,
  val maxtokenprop: Short
) {
  val structName: String = "rewardrules"

  @ForTechUse
  val getMainfunc: FuncinfoPublishStruct
    @ChildCompress
    get() = mainfunc

  @ForTechUse
  val getCurationfunc: FuncinfoPublishStruct
    @ChildCompress
    get() = curationfunc

  @ForTechUse
  val getTimepenalty: FuncinfoPublishStruct
    @ChildCompress
    get() = timepenalty

  @ForTechUse
  val getMaxtokenprop: Short
    @ShortCompress
    get() = maxtokenprop

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishRewardrulesPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
