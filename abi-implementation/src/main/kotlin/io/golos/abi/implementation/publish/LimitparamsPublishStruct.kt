// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Byte
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class LimitparamsPublishStruct(
  val act: Long,
  val charge_id: Byte,
  val price: Long,
  val cutoff: Long,
  val vesting_price: Long,
  val min_vesting: Long
) {
  val structName: String = "limitparams"

  @ForTechUse
  val getAct: Long
    @LongCompress
    get() = act

  @ForTechUse
  val getChargeId: Byte
    @ByteCompress
    get() = charge_id

  @ForTechUse
  val getPrice: Long
    @LongCompress
    get() = price

  @ForTechUse
  val getCutoff: Long
    @LongCompress
    get() = cutoff

  @ForTechUse
  val getVestingPrice: Long
    @LongCompress
    get() = vesting_price

  @ForTechUse
  val getMinVesting: Long
    @LongCompress
    get() = min_vesting

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishLimitparamsPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
