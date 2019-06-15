import com.memtrip.eos.abi.writer.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.moshi.Moshi
import io.golos.cyber4j.model.AbiStruct
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.model.EosAbi
import io.golos.cyber4j.utils.CyberNameAdapter
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.lang.reflect.Type
import java.math.BigInteger
import kotlin.reflect.KClass

class Misc {}

private val moshi = Moshi.Builder().add(CyberName::class.java, CyberNameAdapter()).build()!!

fun main(args: Array<String>) {
    val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()


    val resp = okHttpClient.newCall(Request.Builder()
            .post(RequestBody.create(MediaType.get("application/json"),
                    moshi.toJson(mapOf("account_name" to "gls.publish"))))
            .url("http://46.4.96.246:8888/v1/chain/get_abi")
            .build())
            .execute()

    val respBody = resp.body()!!.string()
    println(respBody)

    val abiObject = moshi.fromJson<EosAbi>(respBody)!!

    val file = File(".", "gls.json")

    file.writeText(moshi.toJson(abiObject))
}


private inline fun <reified T> Moshi.fromJson(json: String) = adapter<T>(T::class.java).fromJson(json)
private inline fun <reified T> Moshi.toJson(`object`: T) = adapter<T>(T::class.java).toJson(`object`)


private fun createBuildInTypesMap() = hashMapOf(*ClassName("kotlin", "String").createVariations("string"),
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
            putAll(EosBool::class.asTypeName().createVariations("bool"))
            putAll(EosAsset::class.asTypeName().createVariations("asset"))
            putAll(EosSymbolCode::class.asTypeName().createVariations("symbol_code"))
            putAll(EosSymbol::class.asTypeName().createVariations("symbol"))
        }

private val simpleTypeToAnnotationsMap = mapOf<TypeName, KClass<*>>(
        Short::class.asTypeName() to ShortCompress::class,
        Int::class.asTypeName() to IntCompress::class,
        Long::class.asTypeName() to LongCompress::class,
        Byte::class.asTypeName() to ByteCompress::class,
        ByteArray::class.asTypeName() to BytesCompress::class,
        BigInteger::class.asTypeName() to BytesCompress::class,
        String::class.asTypeName() to StringCompress::class,
        BigInteger::class.asTypeName() to BytesCompress::class,
        CyberName::class.asTypeName() to NameCompress::class,
        EosBool::class.asTypeName() to ByteCompress::class,
        EosAsset::class.asTypeName() to AssetCompress::class,
        EosSymbolCode::class.asTypeName() to ByteCompress::class,
        EosSymbol::class.asTypeName() to StringCompress::class,
        ClassName("com.memtrip.eos.core.crypto", "EosPublicKey") to PublicKeyCompress::class)

fun main() {
    val packageName = Misc::class.java.`package`.name + ".generated"

    val eosAbi = moshi.fromJson<EosAbi>(File("", "gls.json").readText())!!

    val buildInTypes = createBuildInTypesMap()

    val variantsMap = eosAbi
            .abi
            .variants.map {
        val interfaceName = it.name.toClassName("Interface")

        buildInTypes.putAll(ClassName(packageName, interfaceName).createVariations(it.name))
        interfaceName to it.types.map { it.toClassName() }
    }
            .toMap()
    val s = File.separator
    val folder = File("",
            "src${s}main${s}java")

    variantsMap.keys.forEach {
        FileSpec.builder(Misc::class.java.`package`.name + ".generated", it)
                .addType(TypeSpec
                        .interfaceBuilder(it)
                        .addAnnotation(Abi::class.asTypeName())
                        .build())
                .build().writeTo(folder)
    }

    eosAbi.abi.types.forEach {
        val type = it.type
        val resolvedClassName =
                if (!buildInTypes.containsKey(it.type) && type.matches(integerRegex)) {
                    intStringToClassName(it.type)
                } else buildInTypes[it.type]
                        ?: throw IllegalStateException("type ${it.type} not found")
        buildInTypes.putAll(resolvedClassName.createVariations(it.new_type_name))
    }



    eosAbi.abi.structs.forEach {
        buildInTypes.putAll(ClassName(packageName,
                it.generateClassName()).createVariations(it.name))
    }

    eosAbi.abi.structs.forEach { abiStruct ->
        val className = abiStruct.generateClassName()

        val classFile = FileSpec.builder(packageName, className)
                .also { fileBuilder ->
                    aliasesSet.forEach {
                        fileBuilder.addImport(it, "")
                    }
                    fileBuilder.addImport("asByteArray", "")
                }
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
                                                buildInTypes[stuctField.type]
                                                        ?: throw java.lang.IllegalStateException("cannot find ClassName for type ${stuctField.type}"))
                                    }
                                }
                                .build())
                        .also { builder: TypeSpec.Builder ->
                            abiStruct.fields.forEach { structField ->
                                builder.addProperty(
                                        PropertySpec
                                                .builder(structField.name,
                                                        buildInTypes[structField.type]!!)
                                                .initializer(structField.name)
                                                .build())
                            }
                        }
                        .also { builder: TypeSpec.Builder ->
                            abiStruct.fields.forEach { structField ->
                                val typeName = buildInTypes[structField.type]!!
                                builder.addProperty(
                                        PropertySpec.builder("get${structField.name.fromSnakeCase().capitalize()}",
                                                if (typeName.isNullable) ByteArray::class.asTypeName()
                                                else when (typeName) {
                                                    BigInteger::class.asTypeName() -> ByteArray::class.asTypeName()
                                                    CyberName::class.asTypeName() -> String::class.asTypeName()
                                                    EosBool::class.asTypeName() -> Byte::class.asTypeName()
                                                    EosAsset::class.asTypeName() -> String::class.asTypeName()
                                                    EosSymbolCode::class.asTypeName() -> Byte::class.asTypeName()
                                                    EosSymbol::class.asTypeName() -> String::class.asTypeName()
                                                    else -> typeName
                                                })
                                                .getter(FunSpec.getterBuilder()
                                                        .addStatement(
                                                                when {
                                                                    typeName.isNullable -> "return ${structField.name}.asByteArray()"
                                                                    aliasesSet.contains(typeName) -> "return ${structField.name}.value"
                                                                    else -> when (typeName) {
                                                                        BigInteger::class.asTypeName() ->
                                                                            "return ByteArray(16) { 0 }.also { System.arraycopy(${structField.name}.toByteArray(), 0, it, 0, ${structField.name}.toByteArray().size) }.reversedArray()"

                                                                        CyberName::class.asTypeName() -> "return ${structField.name}.name"
                                                                        else -> "return ${structField.name}"
                                                                    }
                                                                })
                                                        .addAnnotation(
                                                                when {
                                                                    typeName.isNullable -> BytesCompress::class
                                                                    simpleTypeToAnnotationsMap.containsKey(typeName) -> simpleTypeToAnnotationsMap.getValue(typeName)
                                                                    else -> {
                                                                        if (typeName is ClassName) {
                                                                            ChildCompress::class
                                                                        } else if (typeName is ParameterizedTypeName) {
                                                                            if (typeName.typeArguments.size != 1) throw IllegalArgumentException("wrong type arguments size, now ${typeName.typeArguments}")

                                                                            if (typeName.typeArguments.firstOrNull() == String::class.asTypeName()) {
                                                                                StringCollectionCompress::class
                                                                            } else CollectionCompress::class

                                                                        } else throw java.lang.IllegalStateException("cannot find right annotation for " +
                                                                                "type $typeName")
                                                                    }
                                                                }

                                                        )
                                                        .build())
                                                .build())
                            }
                        }
                        .build())
                .build()
        classFile.writeTo(folder)
    }
}

private fun AbiStruct.generateClassName() = this.name.toClassName()

private fun String.toClassName(postfix: String = "Struct") = "${this.fromSnakeCase().capitalize()}$postfix"

private fun String.fromSnakeCase() = this
        .split("_")
        .joinToString("")
        { it.capitalize() }

private fun TypeName.createVariations(forName: String): Array<Pair<String, TypeName>> =
        arrayOf(forName to this,
                forName.plus("?") to this.copy(true),
                "$forName[]" to List::class.asTypeName().parameterizedBy(this))


private val integerRegex = Regex("(u)?int(\\d){1,3}")

private fun intStringToClassName(integerString: String): ClassName {
    if (!integerString.matches(integerRegex)) throw IllegalArgumentException("string $integerString not matches with $integerRegex regexp")
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

