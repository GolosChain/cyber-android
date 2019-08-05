// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.memo

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.PublicKeyCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.memtrip.eos.core.crypto.EosPublicKey
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class MemoKeyMemoStruct(
  val name: CyberName,
  val key: EosPublicKey
) {
  val structName: String = "memo_key"

  @ForTechUse
  val getName: CyberName
    @CyberNameCompress
    get() = name

  @ForTechUse
  val getKey: EosPublicKey
    @PublicKeyCompress
    get() = key

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishMemoKeyMemoStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
