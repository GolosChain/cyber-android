// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.referral

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
data class ReferralStateReferralStruct(
  val id: Long,
  val breakout_params: BreakoutParametrsReferralStruct,
  val expire_params: ExpireParametrsReferralStruct,
  val percent_params: PercentParametrsReferralStruct
) {
  val structName: String = "referral_state"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getBreakoutParams: BreakoutParametrsReferralStruct
    @ChildCompress
    get() = breakout_params

  @ForTechUse
  val getExpireParams: ExpireParametrsReferralStruct
    @ChildCompress
    get() = expire_params

  @ForTechUse
  val getPercentParams: PercentParametrsReferralStruct
    @ChildCompress
    get() = percent_params

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishReferralStateReferralStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
