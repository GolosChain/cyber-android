// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.token

import com.memtrip.eos.abi.writer.Abi
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
data class BulktransferTokenStruct(
  val from: CyberName,
  val recipients: List<RecipientTokenStruct>
) {
  val structName: String = "bulktransfer"

  @ForTechUse
  val getFrom: CyberName
    @CyberNameCompress
    get() = from

  @ForTechUse
  val getRecipients: List<RecipientTokenStruct>
    @CollectionCompress
    get() = recipients

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishBulktransferTokenStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
