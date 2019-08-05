// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.BytesCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberAsset
import java.math.BigInteger
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class PoolstatePublishStruct(
  val msgs: Long,
  val funds: CyberAsset,
  val rshares: BigInteger,
  val rsharesfn: BigInteger
) {
  val structName: String = "poolstate"

  @ForTechUse
  val getMsgs: Long
    @LongCompress
    get() = msgs

  @ForTechUse
  val getFunds: CyberAsset
    @AssetCompress
    get() = funds

  @ForTechUse
  val getRshares: ByteArray
    @BytesCompress
    get() = ByteArray(16) { 0 }.also { System.arraycopy(rshares.toByteArray(), 0, it, 0,
        rshares.toByteArray().size) }.reversedArray()

  @ForTechUse
  val getRsharesfn: ByteArray
    @BytesCompress
    get() = ByteArray(16) { 0 }.also { System.arraycopy(rsharesfn.toByteArray(), 0, it, 0,
        rsharesfn.toByteArray().size) }.reversedArray()

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishPoolstatePublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
