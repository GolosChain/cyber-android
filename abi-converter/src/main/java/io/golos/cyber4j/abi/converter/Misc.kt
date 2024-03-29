package io.golos.cyber4j.abi.converter

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import io.golos.cyber4j.abi.writer.*
import io.golos.cyber4j.abi.writer.compression.CompressionType
import io.golos.cyber4j.annotations.ForTechUse
import io.golos.cyber4j.chain.actions.transaction.abi.ActionAbi
import io.golos.cyber4j.chain.actions.transaction.abi.TransactionAuthorizationAbi
import io.golos.cyber4j.sharedmodel.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.math.BigInteger
import java.util.*


class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val srcDir = args[0]

            val buildDir = File(args[1])

            val contracts = args.toList().subList(2, args.size)

            println("src dir = $srcDir, buildDir = $buildDir, contracts = $contracts")

            val abis = contracts.map { getAbi(CyberName(it), buildDir) }

            val destPackage = "io.golos.cyber4j.abi.implementation"

            val generatedFileSpec = abis
                    .map { abi: EosAbi ->
                        val abiPackage = destPackage + "." + abi.toContractName().name

                        (generateClasses(abi.toContractName(),
                                abi,
                                abiPackage,
                                "Cyber")
                                + generateActions(abi, abiPackage)
                                )
                    }.flatten()
            generatedFileSpec.forEach {
                it.writeTo(File(srcDir))
            }
        }
    }
}


private val moshi = Moshi.Builder().add(CyberName::class.java, CyberNameAdapter()).build()!!

fun getAbi(contractName: CyberName, buildDir: File): EosAbi {
    val cashedFile = File(buildDir, "${contractName.name}.json")

    if (cashedFile.exists()) {
        val content = cashedFile.readText()
        return moshi.fromJson<EosAbi>(content)!!
    }
    val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()


    val resp = okHttpClient.newCall(Request.Builder()
            .post(RequestBody.create(MediaType.get("application/json"),
                    moshi.toJson(mapOf("account_name" to contractName.name))))
            .url("https://node-cyberway.golos.io/v1/chain/get_abi")
            .build())
            .execute()

    val respBody = resp.body()!!.string()

    if (!resp.isSuccessful)
        throw java.lang.IllegalStateException("blockchain said that $contractName does not exists")

    val out = moshi.fromJson<EosAbi>(respBody)!!
    cashedFile.writeText(respBody)

    return out
}

val squishableInterfaceName: ClassName = ISquishable::class.asClassName()

val squishMethodName = "squish"

val structNamePropertyName = "structName"

val getStructIndexFuncName = "getStructIndexForCollectionSquish"

fun generateClasses(contractName: CyberName,
                    eosAbi: EosAbi,
                    packageName: String,
                    writerInterfaceName: String): List<FileSpec> {

    val classPrefix = contractName.name.capitalize()

    val stringToTypesMap = HashMap(builtInTypes)

    val variantsMap = eosAbi
            .abi
            .variants.map {
        val interfaceName = it.name.toClassName("", "Interface")

        stringToTypesMap.putAll(ClassName(packageName, interfaceName).createVariations(it.name))
        interfaceName to it.types.map { it.toClassName(classPrefix) }
    }.toMap()

    val out = variantsMap.keys.map {
        FileSpec.builder(packageName, it)
                .addType(TypeSpec
                        .interfaceBuilder(it)
                        .addSuperinterface(squishableInterfaceName)
                        .addAnnotation(Abi::class.asTypeName())
                        .build())
                .build()
    }

    eosAbi.abi.types.forEach {
        val type = it.type
        val resolvedClassName =
                if (!stringToTypesMap.containsKey(it.type) && type.matches(integerRegex)) {
                    intStringToClassName(it.type)
                } else stringToTypesMap[it.type]
                        ?: throw IllegalStateException("type ${it.type} not found")
        stringToTypesMap.putAll(resolvedClassName.createVariations(it.new_type_name))
    }


    eosAbi.abi.structs.forEach {
        stringToTypesMap.putAll(ClassName(packageName,
                it.generateStructName(classPrefix)).createVariations(it.name))
    }

    return out + eosAbi.abi.structs.map { abiStruct ->
        var isInterfaceImpl = false

        val className = abiStruct.generateStructName(classPrefix)

        val classFile = FileSpec.builder(packageName, className)
                .addComment("Class is generated, changes would be overridden on compile")
                .addImport(CompressionType::class, "")
                .addImport(ActionAbi::class, "")
                .addImport(packageName.dropLastWhile { it != '.' }, "AbiBinaryGen$writerInterfaceName")
                .addType(TypeSpec.classBuilder(className)
                        .also { typeBuilder ->
                            if (variantsMap.values.flatten().contains(className)) {
                                typeBuilder.addSuperinterfaces(variantsMap.filter { it.value.contains(className) }.map {
                                    ClassName(packageName, it.key)
                                })
                                isInterfaceImpl = true
                            }
                        }
                        .addModifiers(KModifier.DATA)
                        .addAnnotation(Abi::class.asTypeName())
                        .addAnnotation(AnnotationSpec.builder(JsonClass::class).addMember("generateAdapter = true").build())
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .also { builder: FunSpec.Builder ->
                                    val fields = resolveStrucFilelds(abiStruct, eosAbi)

                                    fields.forEach { stuctField ->
                                        builder.addParameter(stuctField.name,
                                                stringToTypesMap[stuctField.type]
                                                        ?: throw java.lang.IllegalStateException("cannot find ClassName for type ${stuctField.type}"))
                                    }
                                    if (fields.isEmpty())
                                        builder.addParameter(ParameterSpec.builder("stub",
                                                String::class.asTypeName())
                                                .defaultValue("\"stub\"")
                                                .build())
                                }
                                .build())
                        .also { builder: TypeSpec.Builder ->
                            val fields = resolveStrucFilelds(abiStruct, eosAbi)

                            fields.forEach { structField ->
                                builder.addProperty(
                                        PropertySpec
                                                .builder(structField.name,
                                                        stringToTypesMap[structField.type]!!)
                                                .initializer(structField.name)
                                                .build())
                            }

                            builder.addProperty(PropertySpec.builder(structNamePropertyName, String::class)
                                    .initializer("\"${abiStruct.name}\"")
                                    .build())

                            if (isInterfaceImpl) {
                                val variants = eosAbi.abi.variants
                                val index = variants.find { it.types.contains(abiStruct.name) }!!
                                        .types.indexOf(abiStruct.name)
                                if (index < 0) throw IllegalStateException("index cannot be -1")

                                builder.addFunction(FunSpec.builder(getStructIndexFuncName)
                                        .returns(Int::class)
                                        .addAnnotation(ForTechUse::class)
                                        .addModifiers(KModifier.OVERRIDE)
                                        .addCode("return $index")
                                        .build())
                            }

                            if (fields.isEmpty())
                                builder.addProperty(PropertySpec.builder("stub",
                                        String::class.asTypeName())
                                        .initializer("stub")
                                        .build())
                        }
                        .also { builder: TypeSpec.Builder ->
                            val fields = resolveStrucFilelds(abiStruct, eosAbi)

                            fields.forEach { structField ->
                                val typeName = stringToTypesMap[structField.type]!!
                                builder.addProperty(
                                        PropertySpec.builder("get${structField.name.fromSnakeCase().capitalize()}",
                                                when (typeName) {
                                                    BigInteger::class.asTypeName() -> ByteArray::class.asTypeName()
                                                    else -> typeName
                                                })
                                                .addAnnotation(ForTechUse::class)
                                                .getter(FunSpec.getterBuilder()
                                                        .addStatement(
                                                                when (typeName) {
                                                                    BigInteger::class.asTypeName() ->
                                                                        "return ByteArray(16) { 0 }.also { System.arraycopy(${structField.name}.toByteArray(), 0, it, 0, ${structField.name}.toByteArray().size) }.reversedArray()"
                                                                    else -> "return ${structField.name}"
                                                                })
                                                        .addAnnotation(
                                                                when {
                                                                    simpleTypeToAnnotationsMap.containsKey(typeName) -> simpleTypeToAnnotationsMap.getValue(typeName)
                                                                    else -> {
                                                                        when (typeName) {
                                                                            is ClassName -> ChildCompress::class
                                                                            is ParameterizedTypeName -> {
                                                                                if (typeName.typeArguments.size != 1) throw IllegalArgumentException("wrong type arguments size, " +
                                                                                        "now ${typeName.typeArguments}")

                                                                                val collectionType = typeName.typeArguments.first()

                                                                                val simpleCollectionType = collectionType.toString().takeLastWhile { it != '.' }
                                                                                if (variantsMap.keys.contains(simpleCollectionType)) InterfaceCollectionCompress::class
                                                                                else when (collectionType) {
                                                                                    String::class.asTypeName() -> StringCollectionCompress::class
                                                                                    Long::class.asTypeName() -> LongCollectionCompress::class
                                                                                    CyberName::class.asTypeName() -> CyberNameCollectionCompress::class
                                                                                    Byte::class.asTypeName() -> ByteCompress::class
                                                                                    else -> CollectionCompress::class
                                                                                }
                                                                            }
                                                                            else -> throw java.lang.IllegalStateException("cannot find right annotation for " +
                                                                                    "type $typeName")
                                                                        }
                                                                    }
                                                                }
                                                        )
                                                        .build())
                                                .build())
                            }
                        }
                        .also { builder ->
                            builder.addFunction(FunSpec
                                    .builder("toHex")
                                    .addCode("""return AbiBinaryGen$writerInterfaceName(CompressionType.NONE)
                                        |               .squish$className(this)
                                        |               .toHex()
                                    """.trimMargin())
                                    .build())
                            if (isInterfaceImpl) {
                                builder.addFunction(FunSpec
                                        .builder(squishMethodName)
                                        .addModifiers(KModifier.OVERRIDE)
                                        .addCode("""return AbiBinaryGen$writerInterfaceName(CompressionType.NONE)
                                        |               .squish$className(this)
                                        |               .toBytes()
                                    """.trimMargin())
                                        .build())
                            }

                            builder.addFunction(FunSpec
                                    .builder("toActionAbi")
                                    .addParameter("contractName", String::class)
                                    .addParameter("actionName", String::class)
                                    .addParameter("transactionAuth", List::class.asClassName().parameterizedBy(TransactionAuthorizationAbi::class.asClassName()))
                                    .addCode("""
                                        |return ActionAbi(contractName, actionName,
                                        |       transactionAuth, toHex())""".trimMargin())
                                    .build())
                        }
                        .build())
                .build()
        classFile
    }
}

fun AbiStruct.generateStructName(prefix: String) = this.name.toClassName(prefix)


