package io.golos.abiconverter

import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.golos.sharedmodel.Cyber4JConfig
import io.golos.sharedmodel.EosAbi

fun generateActions(eosAbi: EosAbi,
                    packageName: String): List<FileSpec> {
    return eosAbi.abi.actions.map { abiAction ->
        val actionName = abiAction.name.toClassName(eosAbi.toContractName().name.capitalize(),
                "Action")

        val abiType = ClassName(packageName,
                abiAction.type.toClassName(eosAbi.toContractName().name.capitalize())
        )
        val structParamName = "struct"

        FileSpec
                .builder(packageName, actionName)
                .addComment("class is generated, and would be overridden on compile")
                .addImport("com.memtrip.eos.chain.actions.transaction.abi",
                        "ActionAbi", "TransactionAuthorizationAbi")
                .addImport("com.memtrip.eos.chain.actions.transaction",
                        "TransactionPusher", "AbiBinaryGenTransactionWriter")
                .addImport("com.memtrip.eos.abi.writer.compression",
                        "CompressionType")
                .addImport("com.memtrip.eos.chain.actions.transaction.misc",
                        "ProvideBandwichAbi")
                .addImport("io.golos.sharedmodel",
                        "CyberName")
                .addType(TypeSpec.classBuilder(actionName)
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .addParameter(structParamName, abiType)
                                .build())
                        .addProperty(PropertySpec.builder(structParamName, abiType)
                                .initializer(structParamName)
                                .build())
                        .addFunction(FunSpec
                                .builder("toActionAbi")
                                .addAnnotation(JvmOverloads::class.java)
                                .addParameter("transactionAuth", List::class.asClassName().parameterizedBy(TransactionAuthorizationAbi::class.asClassName()))
                                .addParameter(
                                        ParameterSpec.builder("contractName", String::class)
                                                .defaultValue(CodeBlock.of("\"${eosAbi.account_name.name}\""))
                                                .build())
                                .addParameter(
                                        ParameterSpec.builder("actionName", String::class)
                                                .defaultValue(CodeBlock.of("\"${abiAction.name}\""))
                                                .build()
                                )

                                .addCode("""return  ActionAbi(contractName, actionName,
            transactionAuth, struct.toHex())""".trimMargin())
                                .build())

                        .addFunction(
                                FunSpec.builder("createBandwidthActionAbi")
                                        .addModifiers(KModifier.PRIVATE)
                                        .addParameter("forUser", String::class.asTypeName())
                                        .addCode("""return ActionAbi("cyber",
            "providebw",
            listOf(TransactionAuthorizationAbi("gls", "providebw")),
            AbiBinaryGenTransactionWriter(CompressionType.NONE)
                    .squishProvideBandwichAbi(
                            ProvideBandwichAbi(
                                    CyberName("gls"),
                                    CyberName(forUser))
                    ).toHex())""".trimIndent())
                                        .build()
                        )
                        .addFunction(
                                FunSpec.builder("createSignedTransactionForProvideBw")
                                        .addAnnotation(JvmOverloads::class.java)
                                        .addParameter("transactionAuth",
                                                List::class.asClassName().parameterizedBy(TransactionAuthorizationAbi::class.asClassName()))
                                        .addParameter("key", EosPrivateKey::class)
                                        .addParameter(
                                                ParameterSpec.builder("withConfig", Cyber4JConfig::class)
                                                        .build())
                                        .addParameter(
                                                ParameterSpec.builder("contractName", String::class)
                                                        .defaultValue(CodeBlock.of("\"${eosAbi.account_name.name}\""))
                                                        .build())
                                        .addParameter(
                                                ParameterSpec.builder("actionName", String::class)
                                                        .defaultValue(CodeBlock.of("\"${abiAction.name}\""))
                                                        .build())
                                        .addCode("""return TransactionPusher.createSignedTransaction(
            listOf(toActionAbi(transactionAuth, contractName, actionName),
                    createBandwidthActionAbi(transactionAuth[0].actor)),
            listOf(key),
            withConfig.blockChainHttpApiUrl,
            withConfig.logLevel,
            withConfig.httpLogger)""")
                                        .build()
                        )
                        .addFunction(FunSpec.builder("push")
                                .addAnnotation(JvmOverloads::class.java)
                                .addParameter("transactionAuth", List::class.asClassName().parameterizedBy(TransactionAuthorizationAbi::class.asClassName()))
                                .addParameter("key", EosPrivateKey::class)
                                .addParameter(
                                        ParameterSpec.builder("withConfig", Cyber4JConfig::class)
                                                .build())
                                .addParameter(
                                        ParameterSpec.builder("provideBandwidth", Boolean::class)
                                                .defaultValue("false")
                                                .build())
                                .addParameter(
                                        ParameterSpec.builder("bandwidthProviderKey", EosPrivateKey::class.asTypeName().copy(true))
                                                .defaultValue("null")
                                                .build()
                                )
                                .addParameter(
                                        ParameterSpec.builder("contractName", String::class)
                                                .defaultValue(CodeBlock.of("\"${eosAbi.account_name.name}\""))
                                                .build())
                                .addParameter(
                                        ParameterSpec.builder("actionName", String::class)
                                                .defaultValue(CodeBlock.of("\"${abiAction.name}\""))
                                                .build())

                                .addCode("""return TransactionPusher.pushTransaction(arrayListOf(toActionAbi(transactionAuth,
            contractName, actionName)).apply { if (provideBandwidth) this.add(createBandwidthActionAbi(transactionAuth[0].actor)) },
            key, struct::class.java,
            withConfig.blockChainHttpApiUrl, provideBandwidth, bandwidthProviderKey,
            withConfig.logLevel,
            withConfig.httpLogger)""".trimIndent())

                                .build())
                        .build())
                .build()
    }
}