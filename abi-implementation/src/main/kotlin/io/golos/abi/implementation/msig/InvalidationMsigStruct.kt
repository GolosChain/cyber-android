// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

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
import io.golos.sharedmodel.CyberTimeStampMicroseconds
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class InvalidationMsigStruct(
  val account: CyberName,
  val last_invalidation_time: CyberTimeStampMicroseconds
) {
  val structName: String = "invalidation"

  @ForTechUse
  val getAccount: CyberName
    @CyberNameCompress
    get() = account

  @ForTechUse
  val getLastInvalidationTime: CyberTimeStampMicroseconds
    @LongCompress
    get() = last_invalidation_time

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishInvalidationMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
