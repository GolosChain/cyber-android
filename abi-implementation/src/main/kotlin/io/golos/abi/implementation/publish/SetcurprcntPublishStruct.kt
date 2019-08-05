// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class SetcurprcntPublishStruct(
  val message_id: MssgidPublishStruct,
  val curators_prcnt: Short
) {
  val structName: String = "setcurprcnt"

  @ForTechUse
  val getMessageId: MssgidPublishStruct
    @ChildCompress
    get() = message_id

  @ForTechUse
  val getCuratorsPrcnt: Short
    @ShortCompress
    get() = curators_prcnt

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishSetcurprcntPublishStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
