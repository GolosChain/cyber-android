// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.ctrl

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCollectionCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.TimestampCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberTimeStampSeconds
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class MsigAuthsCtrlStruct(
  val id: Long,
  val witnesses: List<CyberName>,
  val last_update: CyberTimeStampSeconds
) {
  val structName: String = "msig_auths"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getWitnesses: List<CyberName>
    @CyberNameCollectionCompress
    get() = witnesses

  @ForTechUse
  val getLastUpdate: CyberTimeStampSeconds
    @TimestampCompress
    get() = last_update

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMsigAuthsCtrlStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
