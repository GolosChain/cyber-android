// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.StringCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.Long
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class VoteEventPublishStruct(
  val voter: CyberName,
  val author: CyberName,
  val permlink: String,
  val weight: Short,
  val curatorsw: Long,
  val rshares: Long
) {
  val structName: String = "vote_event"

  @ForTechUse
  val getVoter: CyberName
    @CyberNameCompress
    get() = voter

  @ForTechUse
  val getAuthor: CyberName
    @CyberNameCompress
    get() = author

  @ForTechUse
  val getPermlink: String
    @StringCompress
    get() = permlink

  @ForTechUse
  val getWeight: Short
    @ShortCompress
    get() = weight

  @ForTechUse
  val getCuratorsw: Long
    @LongCompress
    get() = curatorsw

  @ForTechUse
  val getRshares: Long
    @LongCompress
    get() = rshares

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVoteEventPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
