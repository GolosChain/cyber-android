package io.golos.cyber4j.abi.converter

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.moshi.Moshi
import io.golos.cyber4j.abi.writer.*
import io.golos.cyber4j.core.crypto.EosPublicKey
import io.golos.cyber4j.sharedmodel.*
import java.lang.reflect.Type
import java.math.BigInteger
import kotlin.reflect.KClass

fun EosAbi.toContractName(): CyberName {
    val splited = this.account_name.name.split(".")
    if (splited.size != 2) throw IllegalStateException("wrong contract name $this")
    return CyberName(splited[1])
}

fun EosAbi.toContractAccount() = CyberName(this.account_name.name.split(".")[0])

val integerRegex = Regex("(u)?int(\\d){1,3}")

inline fun <reified T> Moshi.fromJson(json: String) = adapter<T>(T::class.java).nonNull().fromJson(json)

inline fun <reified T> Moshi.toJson(`object`: T) = adapter<T>(T::class.java).toJson(`object`)

val builtInTypes = hashMapOf(*ClassName("kotlin", "String").createVariations("string"),
        *(CyberName::class.java.asTypeName() as ClassName).createVariations("name"),
        * (arrayOf("int8", "uint8", "int16", "uint16", "int32", "uint32",
                "int64", "uint64", "int128", "uint128")
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
            putAll(ByteArray::class.asTypeName().createVariations("bytes"))
            putAll(CyberTimeStampSeconds::class.asTypeName().createVariations("time_point_sec"))
            putAll(CheckSum256::class.asTypeName().createVariations("checksum256"))
            putAll(CheckSum256::class.asTypeName().createVariations("checksum256$"))
            putAll(CyberTimeStampMicroseconds::class.asTypeName().createVariations("time_point"))
            putAll(Varuint::class.asTypeName().createVariations("varuint32"))
            putAll(EosPublicKey::class.asTypeName().createVariations("public_key"))
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
        CyberName::class.asTypeName().copy(true) to NullableCyberNameCompress::class,
        CyberAsset::class.asTypeName() to AssetCompress::class,
        CyberAsset::class.asTypeName().copy(true) to NullableAssetCompress::class,
        CyberSymbolCode::class.asTypeName() to SymbolCodeCompress::class,
        CyberSymbol::class.asTypeName() to SymbolCompress::class,
        CyberTimeStampSeconds::class.asTypeName() to TimestampCompress::class,
        CyberTimeStampMicroseconds::class.asTypeName() to LongCompress::class,
        CheckSum256::class.asTypeName() to CheckSumCompress::class,
        Varuint::class.asTypeName() to VariableUIntCompress::class,
        EosPublicKey::class.asTypeName() to PublicKeyCompress::class)

fun String.toClassName(prefix: String, postfix: String = "Struct") = "${this.fromSnakeCase().capitalize()}$prefix$postfix"

fun String.fromSnakeCase() = this
        .split("_")
        .joinToString("")
        { it.capitalize() }

fun TypeName.createVariations(forName: String): Array<Pair<String, TypeName>> =
        arrayOf(forName to this,
                forName.plus("?") to this.copy(true),
                "$forName[]" to
                        if (forName == "int8" || forName == "uint8") ByteArray::class.asTypeName() else List::class.asTypeName().parameterizedBy(this))


fun intStringToClassName(integerString: String): ClassName {
    if (!integerString
                    .matches(integerRegex))
        throw IllegalArgumentException("string $integerString not matches with $integerRegex regexp")
    val intSize = Integer.parseInt(integerString.replace("\\D".toRegex(), ""))
    return when {
        intSize <= 8 -> Byte::class.asClassName()
        intSize <= 16 -> Short::class.asClassName()
        intSize <= 32 -> Int::class.asClassName()
        intSize <= 64 -> Long::class.asClassName()
        else -> BigInteger::class.asClassName()
    }
}

fun Type.asClassName() = asTypeName() as ClassName

fun resolveStrucFilelds(abiStruct: AbiStruct, eosAbi: EosAbi) = (if (abiStruct.base.isNotEmpty())
    eosAbi.abi.structs.find { it.name == abiStruct.base }?.fields.orEmpty()
else emptyList())
        .plus(abiStruct.fields)