// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.vesting

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CyberNameCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberName
import kotlin.Int
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class VestingBwproviderVestingStruct(
  val actor: CyberName,
  val permission: CyberName
) : VestingParamInterface {
  val structName: String = "vesting_bwprovider"

  @ForTechUse
  val getActor: CyberName
    @CyberNameCompress
    get() = actor

  @ForTechUse
  val getPermission: CyberName
    @CyberNameCompress
    get() = permission

  @ForTechUse
  override fun getStructIndexForCollectionSquish(): Int = 3
  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVestingBwproviderVestingStruct(this)
                 .toHex()
  override fun squish() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishVestingBwproviderVestingStruct(this)
                 .toBytes()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
