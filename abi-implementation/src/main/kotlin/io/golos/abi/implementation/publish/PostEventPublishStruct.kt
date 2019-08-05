// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.StringCompress
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
data class PostEventPublishStruct(
  val author: CyberName,
  val permlink: String,
  val netshares: Long,
  val voteshares: Long,
  val sumcuratorsw: Long,
  val sharesfn: Long
) {
  val structName: String = "post_event"

  @ForTechUse
  val getAuthor: CyberName
    @CyberNameCompress
    get() = author

  @ForTechUse
  val getPermlink: String
    @StringCompress
    get() = permlink

  @ForTechUse
  val getNetshares: Long
    @LongCompress
    get() = netshares

  @ForTechUse
  val getVoteshares: Long
    @LongCompress
    get() = voteshares

  @ForTechUse
  val getSumcuratorsw: Long
    @LongCompress
    get() = sumcuratorsw

  @ForTechUse
  val getSharesfn: Long
    @LongCompress
    get() = sharesfn

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishPostEventPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
