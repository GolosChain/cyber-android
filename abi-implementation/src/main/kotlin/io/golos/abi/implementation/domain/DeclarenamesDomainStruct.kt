// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.domain

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class DeclarenamesDomainStruct(
  val domains: List<NameInfoDomainStruct>
) {
  val structName: String = "declarenames"

  @ForTechUse
  val getDomains: List<NameInfoDomainStruct>
    @CollectionCompress
    get() = domains

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishDeclarenamesDomainStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
