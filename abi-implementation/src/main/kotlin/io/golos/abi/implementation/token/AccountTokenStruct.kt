// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.token

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class AccountTokenStruct(
  val balance: CyberAsset,
  val payments: CyberAsset
) {
  val structName: String = "account"

  @ForTechUse
  val getBalance: CyberAsset
    @AssetCompress
    get() = balance

  @ForTechUse
  val getPayments: CyberAsset
    @AssetCompress
    get() = payments

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishAccountTokenStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
