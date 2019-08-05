// class is generated, and would be overridden on compile
package io.golos.abi.implementation.stake

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.AbiBinaryGenTransactionWriter
import com.memtrip.eos.chain.actions.transaction.TransactionPusher
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.memtrip.eos.chain.actions.transaction.misc.ProvideBandwichAbi
import com.memtrip.eos.core.crypto.EosPrivateKey
import io.golos.sharedmodel.Cyber4JConfig
import io.golos.sharedmodel.CyberName
import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmOverloads

class ClaimStakeAction(
  val struct: ClaimArgsStakeStruct
) {
  @JvmOverloads
  fun toActionAbi(
    transactionAuth: List<TransactionAuthorizationAbi>,
    contractName: String = "cyber.stake",
    actionName: String = "claim"
  ) =  ActionAbi(contractName, actionName,
              transactionAuth, struct.toHex())
  private fun createBandwidthActionAbi(forUser: String) = ActionAbi("cyber",
              "providebw",
              listOf(TransactionAuthorizationAbi("gls", "providebw")),
              AbiBinaryGenTransactionWriter(CompressionType.NONE)
                      .squishProvideBandwichAbi(
                              ProvideBandwichAbi(
                                      CyberName("gls"),
                                      CyberName(forUser))
                      ).toHex())
  @JvmOverloads
  fun createSignedTransactionForProvideBw(
    transactionAuth: List<TransactionAuthorizationAbi>,
    key: EosPrivateKey,
    withConfig: Cyber4JConfig,
    contractName: String = "cyber.stake",
    actionName: String = "claim"
  ) = TransactionPusher.createSignedTransaction(
              listOf(toActionAbi(transactionAuth, contractName, actionName),
                      createBandwidthActionAbi(transactionAuth[0].actor)),
              listOf(key),
              withConfig.blockChainHttpApiUrl,
              withConfig.logLevel,
              withConfig.httpLogger)
  @JvmOverloads
  fun push(
    transactionAuth: List<TransactionAuthorizationAbi>,
    key: EosPrivateKey,
    withConfig: Cyber4JConfig,
    provideBandwidth: Boolean = false,
    bandwidthProviderKey: EosPrivateKey? = null,
    contractName: String = "cyber.stake",
    actionName: String = "claim"
  ) = TransactionPusher.pushTransaction(arrayListOf(toActionAbi(transactionAuth,
              contractName, actionName)).apply { if (provideBandwidth)
      this.add(createBandwidthActionAbi(transactionAuth[0].actor)) },
              key, struct::class.java,
              withConfig.blockChainHttpApiUrl, provideBandwidth, bandwidthProviderKey,
              withConfig.logLevel,
              withConfig.httpLogger)}
