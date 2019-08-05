// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.IntCompress
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
import kotlin.Int
import kotlin.Long
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class PermlinkPublishStruct(
  val id: Long,
  val parentacc: CyberName,
  val parent_id: Long,
  val value: String,
  val level: Short,
  val childcount: Int
) {
  val structName: String = "permlink"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getParentacc: CyberName
    @CyberNameCompress
    get() = parentacc

  @ForTechUse
  val getParentId: Long
    @LongCompress
    get() = parent_id

  @ForTechUse
  val getValue: String
    @StringCompress
    get() = value

  @ForTechUse
  val getLevel: Short
    @ShortCompress
    get() = level

  @ForTechUse
  val getChildcount: Int
    @IntCompress
    get() = childcount

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishPermlinkPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
