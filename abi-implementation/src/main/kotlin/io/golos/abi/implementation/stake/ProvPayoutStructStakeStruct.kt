// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.stake

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.SymbolCodeCompress
import com.memtrip.eos.abi.writer.TimestampCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberSymbolCode
import io.golos.sharedmodel.CyberTimeStampSeconds
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class ProvPayoutStructStakeStruct(
  val id: Long,
  val token_code: CyberSymbolCode,
  val grantor_name: CyberName,
  val recipient_name: CyberName,
  val amount: Long,
  val date: CyberTimeStampSeconds
) {
  val structName: String = "prov_payout_struct"

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getTokenCode: CyberSymbolCode
    @SymbolCodeCompress
    get() = token_code

  @ForTechUse
  val getGrantorName: CyberName
    @CyberNameCompress
    get() = grantor_name

  @ForTechUse
  val getRecipientName: CyberName
    @CyberNameCompress
    get() = recipient_name

  @ForTechUse
  val getAmount: Long
    @LongCompress
    get() = amount

  @ForTechUse
  val getDate: CyberTimeStampSeconds
    @TimestampCompress
    get() = date

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishProvPayoutStructStakeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
