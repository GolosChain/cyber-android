// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Int
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class StCashoutWindowPublishStruct(
  val window: Int,
  val upvote_lockout: Int
) : PostingParamInterface {
  val structName: String = "st_cashout_window"

  @ForTechUse
  val getWindow: Int
    @IntCompress
    get() = window

  @ForTechUse
  val getUpvoteLockout: Int
    @IntCompress
    get() = upvote_lockout

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 1
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStCashoutWindowPublishStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStCashoutWindowPublishStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
