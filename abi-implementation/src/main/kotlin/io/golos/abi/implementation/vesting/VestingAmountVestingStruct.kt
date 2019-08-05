// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

import com.memtrip.eos.abi.writer.Abi
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
data class VestingAmountVestingStruct(
  val min_amount: Long
) : VestingParamInterface {
  val structName: String = "vesting_amount"

  @ForTechUse
  val getMinAmount: Long
    @LongCompress
    get() = min_amount

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 1
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVestingAmountVestingStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVestingAmountVestingStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
