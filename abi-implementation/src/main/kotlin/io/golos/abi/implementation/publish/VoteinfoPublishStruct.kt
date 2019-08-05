// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.Byte
import kotlin.Long
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class VoteinfoPublishStruct(
  val id: Long,
  val message_id: Long,
  val voter: CyberName,
  val weight: Short,
  val time: Long,
  val count: Byte,
  val delegators: List<DelegateVoterPublishStruct>,
  val curatorsw: Long,
  val rshares: Long,
  val paid_amount: Long
) {
  val structName: String = "voteinfo"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getMessageId: Long
    @LongCompress
    get() = message_id

  @ForTechUse
  val getVoter: CyberName
    @CyberNameCompress
    get() = voter

  @ForTechUse
  val getWeight: Short
    @ShortCompress
    get() = weight

  @ForTechUse
  val getTime: Long
    @LongCompress
    get() = time

  @ForTechUse
  val getCount: Byte
    @ByteCompress
    get() = count

  @ForTechUse
  val getDelegators: List<DelegateVoterPublishStruct>
    @CollectionCompress
    get() = delegators

  @ForTechUse
  val getCuratorsw: Long
    @LongCompress
    get() = curatorsw

  @ForTechUse
  val getRshares: Long
    @LongCompress
    get() = rshares

  @ForTechUse
  val getPaidAmount: Long
    @LongCompress
    get() = paid_amount

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVoteinfoPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
