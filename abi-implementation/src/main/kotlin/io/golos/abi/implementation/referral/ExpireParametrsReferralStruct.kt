// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.referral

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
data class ExpireParametrsReferralStruct(
  val max_expire: Long
) : ReferralParamInterface {
  val structName: String = "expire_parametrs"

  @ForTechUse
  val getMaxExpire: Long
    @LongCompress
    get() = max_expire

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 1
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishExpireParametrsReferralStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishExpireParametrsReferralStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
