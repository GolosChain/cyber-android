// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.ByteArray
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class ExtensionMsigStruct(
  val type: Short,
  val data: ByteArray
) {
  val structName: String = "extension"

  @ForTechUse
  val getType: Short
    @ShortCompress
    get() = type

  @ForTechUse
  val getData: ByteArray
    @BytesCompress
    get() = data

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishExtensionMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
