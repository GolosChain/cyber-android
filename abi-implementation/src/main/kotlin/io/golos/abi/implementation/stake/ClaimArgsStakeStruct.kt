// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.stake

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.SymbolCodeCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberSymbolCode
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class ClaimArgsStakeStruct(
  val grantor_name: CyberName,
  val recipient_name: CyberName,
  val token_code: CyberSymbolCode
) {
  val structName: String = "claim_args"

  @ForTechUse
  val getGrantorName: CyberName
    @CyberNameCompress
    get() = grantor_name

  @ForTechUse
  val getRecipientName: CyberName
    @CyberNameCompress
    get() = recipient_name

  @ForTechUse
  val getTokenCode: CyberSymbolCode
    @SymbolCodeCompress
    get() = token_code

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishClaimArgsStakeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
