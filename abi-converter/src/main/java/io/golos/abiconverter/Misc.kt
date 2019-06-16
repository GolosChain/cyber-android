package io.golos.abiconverter

import com.memtrip.eos.abi.writer.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.moshi.Moshi
import io.golos.sharedmodel.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.lang.reflect.Type
import java.math.BigInteger
import kotlin.reflect.KClass

private val moshi = Moshi.Builder().add(CyberName::class.java, CyberNameAdapter()).build()!!

val integerRegex = Regex("(u)?int(\\d){1,3}")

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

    // val file = File(".", "gls.json")

    //file.writeText(moshi.toJson(abiObject))
}


private inline fun <reified T> Moshi.fromJson(json: String) = adapter<T>(T::class.java).fromJson(json)
private inline fun <reified T> Moshi.toJson(`object`: T) = adapter<T>(T::class.java).toJson(`object`)


val builtInTypes = hashMapOf(*ClassName("kotlin", "String").createVariations("string"),
        *(CyberName::class.java.asTypeName() as ClassName).createVariations("name"),
        * (arrayOf("int8", "uint8", "int16", "uint16", "int32", "uint32", "int64", "uint64")
                .map { it to intStringToClassName(it) })
                .toList()
                .let { listOfPairs ->
                    val copy = ArrayList<Pair<String, TypeName>>(listOfPairs.size)
                    listOfPairs.forEach { pair ->
                        copy.addAll(pair.second.createVariations(pair.first))
                    }
                    copy.toTypedArray()
                })
        .apply {
            putAll(Boolean::class.asTypeName().createVariations("bool"))
            putAll(CyberAsset::class.asTypeName().createVariations("asset"))
            putAll(CyberSymbolCode::class.asTypeName().createVariations("symbol_code"))
            putAll(CyberSymbol::class.asTypeName().createVariations("symbol"))
            putAll(CyberTimeStamp::class.asTypeName().createVariations("time_point_sec"))
            putAll(CheckSum256::class.asTypeName().createVariations("checksum256"))
        } as Map<String, TypeName>

val simpleTypeToAnnotationsMap = mapOf<TypeName, KClass<*>>(
        Boolean::class.asTypeName() to BoolCompress::class,
        Short::class.asTypeName() to ShortCompress::class,
        Short::class.asClassName().copy(true) to NullableShortCompress::class,
        Int::class.asTypeName() to IntCompress::class,
        Long::class.asTypeName() to LongCompress::class,
        Byte::class.asTypeName() to ByteCompress::class,
        ByteArray::class.asTypeName() to BytesCompress::class,
        BigInteger::class.asTypeName() to BytesCompress::class,
        String::class.asTypeName() to StringCompress::class,
        String::class.asClassName().copy(true) to NullableStringCompress::class,
        BigInteger::class.asTypeName() to BytesCompress::class,
        CyberName::class.asTypeName() to CyberNameCompress::class,
        CyberAsset::class.asTypeName() to AssetCompress::class,
        CyberSymbolCode::class.asTypeName() to SymbolCodeCompress::class,
        CyberSymbol::class.asTypeName() to SymbolCompress::class,
        CyberTimeStamp::class.asTypeName() to TimestampCompress::class,
        CheckSum256::class.asTypeName() to CheckSumCompress::class,
        ClassName("com.memtrip.eos.core.crypto", "EosPublicKey") to PublicKeyCompress::class)

fun generateClasses(eosAbi: EosAbi,
                    packageName: String,
                    srcFolder: File) {

    val classPrefix = eosAbi.account_name.name.split(".").getOrElse(1) { "Rand${Math.random()}" }.capitalize()

    val stringToTypesMap = HashMap(builtInTypes)

    val variantsMap = eosAbi
            .abi
            .variants.map {
        val interfaceName = it.name.toClassName("Interface")

        stringToTypesMap.putAll(ClassName(packageName, interfaceName).createVariations(it.name))
        interfaceName to it.types.map { it.toClassName(classPrefix) }
    }.toMap()

    variantsMap.keys.forEach {
        FileSpec.builder(packageName, it)
                .addType(TypeSpec
                        .interfaceBuilder(it)
                        .addAnnotation(Abi::class.asTypeName())
                        .build())
                .build().writeTo(srcFolder)
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
                it.generateClassName(classPrefix)).createVariations(it.name))
    }

    eosAbi.abi.structs.forEach { abiStruct ->
        val className = abiStruct.generateClassName(classPrefix)

        val classFile = FileSpec.builder(packageName, className)
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
                        .build())
                .build()
        classFile.writeTo(srcFolder)
    }
}

private fun AbiStruct.generateClassName(prefix: String) = this.name.toClassName(prefix)

private fun String.toClassName(prefix: String, postfix: String = "Struct") = "${this.fromSnakeCase().capitalize()}$prefix$postfix"

private fun String.fromSnakeCase() = this
        .split("_")
        .joinToString("")
        { it.capitalize() }

private fun TypeName.createVariations(forName: String): Array<Pair<String, TypeName>> =
        arrayOf(forName to this,
                forName.plus("?") to this.copy(true),
                "$forName[]" to List::class.asTypeName().parameterizedBy(this))


private fun intStringToClassName(integerString: String): ClassName {

    println("intStringToClassName $integerRegex")
    if (!integerString
                    .matches(integerRegex))
        throw IllegalArgumentException("string $integerString not matches with $integerRegex regexp")
    val intSize = Integer.parseInt(integerString.replace("\\D".toRegex(), ""))
    return when {
        intSize <= 8 -> Byte::class.java.asClassName()
        intSize <= 16 -> Short::class.java.asClassName()
        intSize <= 32 -> Int::class.java.asClassName()
        intSize <= 64 -> Long::class.java.asClassName()
        else -> BigInteger::class.java.asClassName()
    }
}

private fun Type.asClassName() = asTypeName() as ClassName

