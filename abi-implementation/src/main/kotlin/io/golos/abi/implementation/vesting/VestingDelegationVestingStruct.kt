// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class VestingDelegationVestingStruct(
  val min_amount: Long,
  val min_remainder: Long,
  val return_time: Int,
  val min_time: Int,
  val max_delegators: Int
) : VestingParamInterface {
  val structName: String = "vesting_delegation"

  @ForTechUse
  val getMinAmount: Long
    @LongCompress
    get() = min_amount

  @ForTechUse
  val getMinRemainder: Long
    @LongCompress
    get() = min_remainder

  @ForTechUse
  val getReturnTime: Int
    @IntCompress
    get() = return_time

  @ForTechUse
  val getMinTime: Int
    @IntCompress
    get() = min_time

  @ForTechUse
  val getMaxDelegators: Int
    @IntCompress
    get() = max_delegators

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 2
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVestingDelegationVestingStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVestingDelegationVestingStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
