// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.charge

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.LongCompress
import com.memtrip.eos.abi.writer.SymbolCodeCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberSymbolCode
import kotlin.Byte
import kotlin.Long
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class UseandnotifyChargeStruct(
  val user: CyberName,
  val token_code: CyberSymbolCode,
  val charge_id: Byte,
  val price_arg: Long,
  val id: Long,
  val code: CyberName,
  val action_name: CyberName,
  val cutoff: Long
) {
  val structName: String = "useandnotify"

  @ForTechUse
  val getUser: CyberName
    @CyberNameCompress
    get() = user

  @ForTechUse
  val getTokenCode: CyberSymbolCode
    @SymbolCodeCompress
    get() = token_code

  @ForTechUse
  val getChargeId: Byte
    @ByteCompress
    get() = charge_id

  @ForTechUse
  val getPriceArg: Long
    @LongCompress
    get() = price_arg

  @ForTechUse
  val getId: Long
    @LongCompress
    get() = id

  @ForTechUse
  val getCode: CyberName
    @CyberNameCompress
    get() = code

  @ForTechUse
  val getActionName: CyberName
    @CyberNameCompress
    get() = action_name

  @ForTechUse
  val getCutoff: Long
    @LongCompress
    get() = cutoff

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishUseandnotifyChargeStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
