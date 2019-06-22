package io.golos.abiconverter

import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
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
                                                .defaultValue(CodeBlock.of("\"${eosAbi.toContractName().name}\""))
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
                        .build())


                .build()
    }
}