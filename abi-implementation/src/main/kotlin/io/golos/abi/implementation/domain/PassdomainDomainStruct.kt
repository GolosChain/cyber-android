// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.domain

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.StringCompress
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
data class PassdomainDomainStruct(
  val from: CyberName,
  val to: CyberName,
  val name: String
) {
  val structName: String = "passdomain"

  @ForTechUse
  val getFrom: CyberName
    @CyberNameCompress
    get() = from

  @ForTechUse
  val getTo: CyberName
    @CyberNameCompress
    get() = to

  @ForTechUse
  val getName: String
    @StringCompress
    get() = name

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishPassdomainDomainStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
