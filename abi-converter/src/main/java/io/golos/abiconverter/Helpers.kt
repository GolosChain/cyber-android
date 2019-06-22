package io.golos.abiconverter

import com.memtrip.eos.abi.writer.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.moshi.Moshi
import io.golos.sharedmodel.*
import java.lang.reflect.Type
import java.math.BigInteger
import kotlin.reflect.KClass

fun EosAbi.toContractName() = CyberName(this.account_name.name.split(".")[1])

fun EosAbi.toContractAccount() = CyberName(this.account_name.name.split(".")[0])

val integerRegex = Regex("(u)?int(\\d){1,3}")

inline fun <reified T> Moshi.fromJson(json: String) = adapter<T>(T::class.java).fromJson(json)

inline fun <reified T> Moshi.toJson(`object`: T) = adapter<T>(T::class.java).toJson(`object`)

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

fun String.toClassName(prefix: String, postfix: String = "Struct") = "${this.fromSnakeCase().capitalize()}$prefix$postfix"

fun String.fromSnakeCase() = this
        .split("_")
        .joinToString("")
        { it.capitalize() }

fun TypeName.createVariations(forName: String): Array<Pair<String, TypeName>> =
        arrayOf(forName to this,
                forName.plus("?") to this.copy(true),
                "$forName[]" to List::class.asTypeName().parameterizedBy(this))

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