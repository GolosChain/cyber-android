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
                .addComment("class is generated, and would be overriten on compile")
                .addImport("com.memtrip.eos.chain.actions.transaction.abi",
                        "ActionAbi", "TransactionAuthorizationAbi")
                .addImport("com.memtrip.eos.chain.actions.transaction", "TransactionPusher")
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

                                .addCode("""
                                        |return ActionAbi(contractName, actionName,
                                        |       transactionAuth, $structParamName.toHex())""".trimMargin())
                                .build())

                        .addFunction(FunSpec.builder("push")
                                .addAnnotation(JvmOverloads::class.java)
                                .addParameter("transactionAuth", List::class.asClassName().parameterizedBy(TransactionAuthorizationAbi::class.asClassName()))
                                .addParameter("key", EosPrivateKey::class)
                                .addParameter(
                                        ParameterSpec.builder("contractName", String::class)
                                                .defaultValue(CodeBlock.of("\"${eosAbi.account_name.name}\""))
                                                .build())
                                .addParameter(
                                        ParameterSpec.builder("actionName", String::class)
                                                .defaultValue(CodeBlock.of("\"${abiAction.name}\""))
                                                .build())
                                .addParameter(
                                        ParameterSpec.builder("withConfig", Cyber4JConfig::class)
                                                .defaultValue(CodeBlock.of("Cyber4JConfig.default"))
                                                .build())
                                .addCode("""return TransactionPusher.pushTransaction(listOf(toActionAbi(transactionAuth,
                                    contractName, actionName)), key, struct::class.java, withConfig)""".trimIndent())

                                .build())
                        .build())
                .build()
    }
}