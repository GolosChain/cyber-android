// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class CalcrwrdwtPublishStruct(
  val account: CyberName,
  val mssg_id: Long,
  val post_charge: Long
) {
  val structName: String = "calcrwrdwt"

  @ForTechUse
  val getAccount: CyberName
    @CyberNameCompress
    get() = account

  @ForTechUse
  val getMssgId: Long
    @LongCompress
    get() = mssg_id

  @ForTechUse
  val getPostCharge: Long
    @LongCompress
    get() = post_charge

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishCalcrwrdwtPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
