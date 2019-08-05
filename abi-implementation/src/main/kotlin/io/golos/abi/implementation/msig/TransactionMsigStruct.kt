// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.msig

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ByteCompress
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.ShortCompress
import com.memtrip.eos.abi.writer.TimestampCompress
import com.memtrip.eos.abi.writer.VariableUIntCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import io.golos.sharedmodel.CyberTimeStampSeconds
import io.golos.sharedmodel.Varuint
import kotlin.Byte
import kotlin.Int
import kotlin.Short
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class TransactionMsigStruct(
  val expiration: CyberTimeStampSeconds,
  val ref_block_num: Short,
  val ref_block_prefix: Int,
  val max_net_usage_words: Varuint,
  val max_cpu_usage_ms: Byte,
  val max_ram_kbytes: Varuint,
  val max_storage_kbytes: Varuint,
  val delay_sec: Varuint,
  val context_free_actions: List<ActionMsigStruct>,
  val actions: List<ActionMsigStruct>,
  val transaction_extensions: List<ExtensionMsigStruct>
) {
  val structName: String = "transaction"

  @ForTechUse
  val getExpiration: CyberTimeStampSeconds
    @TimestampCompress
    get() = expiration

  @ForTechUse
  val getRefBlockNum: Short
    @ShortCompress
    get() = ref_block_num

  @ForTechUse
  val getRefBlockPrefix: Int
    @IntCompress
    get() = ref_block_prefix

  @ForTechUse
  val getMaxNetUsageWords: Varuint
    @VariableUIntCompress
    get() = max_net_usage_words

  @ForTechUse
  val getMaxCpuUsageMs: Byte
    @ByteCompress
    get() = max_cpu_usage_ms

  @ForTechUse
  val getMaxRamKbytes: Varuint
    @VariableUIntCompress
    get() = max_ram_kbytes

  @ForTechUse
  val getMaxStorageKbytes: Varuint
    @VariableUIntCompress
    get() = max_storage_kbytes

  @ForTechUse
  val getDelaySec: Varuint
    @VariableUIntCompress
    get() = delay_sec

  @ForTechUse
  val getContextFreeActions: List<ActionMsigStruct>
    @CollectionCompress
    get() = context_free_actions

  @ForTechUse
  val getActions: List<ActionMsigStruct>
    @CollectionCompress
    get() = actions

  @ForTechUse
  val getTransactionExtensions: List<ExtensionMsigStruct>
    @CollectionCompress
    get() = transaction_extensions

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishTransactionMsigStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
