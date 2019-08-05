// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.IntCompress
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
import kotlin.Byte
import kotlin.Int
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class WithdrawalVestingStruct(
  val owner: CyberName,
  val to: CyberName,
  val interval_seconds: Int,
  val remaining_payments: Byte,
  val next_payout: CyberTimeStampSeconds,
  val withdraw_rate: CyberAsset,
  val to_withdraw: CyberAsset
) {
  val structName: String = "withdrawal"

  @ForTechUse
  val getOwner: CyberName
    @CyberNameCompress
    get() = owner

  @ForTechUse
  val getTo: CyberName
    @CyberNameCompress
    get() = to

  @ForTechUse
  val getIntervalSeconds: Int
    @IntCompress
    get() = interval_seconds

  @ForTechUse
  val getRemainingPayments: Byte
    @ByteCompress
    get() = remaining_payments

  @ForTechUse
  val getNextPayout: CyberTimeStampSeconds
    @TimestampCompress
    get() = next_payout

  @ForTechUse
  val getWithdrawRate: CyberAsset
    @AssetCompress
    get() = withdraw_rate

  @ForTechUse
  val getToWithdraw: CyberAsset
    @AssetCompress
    get() = to_withdraw

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishWithdrawalVestingStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
