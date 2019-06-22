package io.golos.abiconverter

import com.memtrip.eos.abi.writer.*
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.moshi.Moshi
import io.golos.sharedmodel.AbiStruct
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberNameAdapter
import io.golos.sharedmodel.EosAbi
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
            val srcDir = args.first()

            val contracts = args.toList().subList(1, args.size)
            println("src dir = $srcDir, contracts = $contracts")

            val abis = contracts.map { getAbi(CyberName(it)) }

            val destPackage = "io.golos.abi.implementation"

            val generatedFileSpec = abis
                    .map { abi: EosAbi ->

                        val abiPackage = destPackage + "." + abi.toContractName().name

                        (generateClasses(abi.toContractName(),
                                abi,
                                abiPackage,
                                "Cyber")
                                + generateActions(abi, abiPackage))
                    }.flatten()
            generatedFileSpec.forEach {
                it.writeTo(File(srcDir))
            }
        }
    }
}


private val moshi = Moshi.Builder().add(CyberName::class.java, CyberNameAdapter()).build()!!

fun getAbi(contractName: CyberName): EosAbi {
    val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()


    val resp = okHttpClient.newCall(Request.Builder()
            .post(RequestBody.create(MediaType.get("application/json"),
                    moshi.toJson(mapOf("account_name" to contractName.name))))
            .url("http://46.4.96.246:8888/v1/chain/get_abi")
            .build())
            .execute()

    val respBody = resp.body()!!.string()
    return moshi.fromJson<EosAbi>(respBody)!!
}


fun generateClasses(contractName: CyberName,
                    eosAbi: EosAbi,
                    packageName: String,
                    writerInterfaceName: String): List<FileSpec> {

    val classPrefix = contractName.name.capitalize()

    val stringToTypesMap = HashMap(builtInTypes)

    val variantsMap = eosAbi
            .abi
            .variants.map {
        val interfaceName = it.name.toClassName("Interface")

        stringToTypesMap.putAll(ClassName(packageName, interfaceName).createVariations(it.name))
        interfaceName to it.types.map { it.toClassName(classPrefix) }
    }.toMap()

    val out = variantsMap.keys.map {
        FileSpec.builder(packageName, it)
                .addType(TypeSpec
                        .interfaceBuilder(it)
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
        val className = abiStruct.generateStructName(classPrefix)

        val classFile = FileSpec.builder(packageName, className)
                .addComment("Class is generated, changes would be overridden on compile")
                .addImport(CompressionType::class, "")
                .addImport("com.memtrip.eos.chain.actions.transaction.abi",
                        "ActionAbi", "TransactionAuthorizationAbi")
                .addImport(packageName.dropLastWhile { it != '.' }, "AbiBinaryGen$writerInterfaceName")
                .addType(TypeSpec.classBuilder(className)
                        .also { typeBuilder ->
                            typeBuilder.addSuperinterfaces(variantsMap.filter { it.value.contains(className) }.map {
                                ClassName(packageName, it.key)
                            })
                        }
                        .addModifiers(KModifier.DATA)
                        .addAnnotation(Abi::class.asTypeName())
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .also { builder: FunSpec.Builder ->
                                    abiStruct.fields.forEach { stuctField ->
                                        builder.addParameter(stuctField.name,
                                                stringToTypesMap[stuctField.type]
                                                        ?: throw java.lang.IllegalStateException("cannot find ClassName for type ${stuctField.type}"))
                                    }
                                    if (abiStruct.fields.isEmpty())
                                        builder.addParameter(ParameterSpec.builder("stub",
                                                String::class.asTypeName(), KModifier.PRIVATE)
                                                .defaultValue("\"stub\"")
                                                .build())
                                }
                                .build())
                        .also { builder: TypeSpec.Builder ->
                            abiStruct.fields.forEach { structField ->
                                builder.addProperty(
                                        PropertySpec
                                                .builder(structField.name,
                                                        stringToTypesMap[structField.type]!!)
                                                .initializer(structField.name)
                                                .build())
                            }
                            if (abiStruct.fields.isEmpty())
                                builder.addProperty(PropertySpec.builder("stub",
                                        String::class.asTypeName())
                                        .initializer("stub")
                                        .build())
                        }
                        .also { builder: TypeSpec.Builder ->
                            abiStruct.fields.forEach { structField ->
                                val typeName = stringToTypesMap[structField.type]!!
                                builder.addProperty(
                                        PropertySpec.builder("get${structField.name.fromSnakeCase().capitalize()}",
                                                when (typeName) {
                                                    BigInteger::class.asTypeName() -> ByteArray::class.asTypeName()
                                                    else -> typeName
                                                })
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

                                                                                when (collectionType) {
                                                                                    String::class.asTypeName() -> StringCollectionCompress::class
                                                                                    Long::class.asTypeName() -> LongCollectionCompress::class
                                                                                    CyberName::class.asTypeName() -> CyberNameCollectionCompress::class
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


