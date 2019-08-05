// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Byte
import kotlin.Int
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class StMaxVoteChangesPublishStruct(
  val value: Byte
) : PostingParamInterface {
  val structName: String = "st_max_vote_changes"

  @ForTechUse
  val getValue: Byte
    @ByteCompress
    get() = value

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 0
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStMaxVoteChangesPublishStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStMaxVoteChangesPublishStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
