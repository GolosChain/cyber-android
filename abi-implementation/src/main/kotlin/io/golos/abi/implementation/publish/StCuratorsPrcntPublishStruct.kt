// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.publish

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.Int
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class StCuratorsPrcntPublishStruct(
  val min_curators_prcnt: Short,
  val max_curators_prcnt: Short
) : PostingParamInterface {
  val structName: String = "st_curators_prcnt"

  @ForTechUse
  val getMinCuratorsPrcnt: Short
    @ShortCompress
    get() = min_curators_prcnt

  @ForTechUse
  val getMaxCuratorsPrcnt: Short
    @ShortCompress
    get() = max_curators_prcnt

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 6
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStCuratorsPrcntPublishStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishStCuratorsPrcntPublishStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
