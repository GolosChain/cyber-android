// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.TimestampCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberTimeStampSeconds
import kotlin.Long
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class DelegationVestingStruct(
  val id: Long,
  val delegator: CyberName,
  val delegatee: CyberName,
  val quantity: CyberAsset,
  val interest_rate: Short,
  val min_delegation_time: CyberTimeStampSeconds
) {
  val structName: String = "delegation"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getDelegator: CyberName
    @CyberNameCompress
    get() = delegator

  @ForTechUse
  val getDelegatee: CyberName
    @CyberNameCompress
    get() = delegatee

  @ForTechUse
  val getQuantity: CyberAsset
    @AssetCompress
    get() = quantity

  @ForTechUse
  val getInterestRate: Short
    @ShortCompress
    get() = interest_rate

  @ForTechUse
  val getMinDelegationTime: CyberTimeStampSeconds
    @TimestampCompress
    get() = min_delegation_time

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishDelegationVestingStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
