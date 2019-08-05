// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class VestingStateVestingStruct(
  val id: Long,
  val withdraw: VestingWithdrawVestingStruct,
  val min_amount: VestingAmountVestingStruct,
  val delegation: VestingDelegationVestingStruct,
  val bwprovider: VestingBwproviderVestingStruct
) {
  val structName: String = "vesting_state"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getWithdraw: VestingWithdrawVestingStruct
    @ChildCompress
    get() = withdraw

  @ForTechUse
  val getMinAmount: VestingAmountVestingStruct
    @ChildCompress
    get() = min_amount

  @ForTechUse
  val getDelegation: VestingDelegationVestingStruct
    @ChildCompress
    get() = delegation

  @ForTechUse
  val getBwprovider: VestingBwproviderVestingStruct
    @ChildCompress
    get() = bwprovider

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVestingStateVestingStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
