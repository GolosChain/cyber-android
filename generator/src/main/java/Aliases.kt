import com.squareup.kotlinpoet.asTypeName
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.model.CyberwayByteWriter

data class EosBool(val value: Byte)

data class EosAsset(val value: String)

data class EosSymbolCode(val value: Byte)

data class EosSymbol(val value: String)


val aliasesSet = setOf(EosBool::class.asTypeName(), EosAsset::class.asTypeName(), EosSymbolCode::class.asTypeName(),
        EosSymbol::class.asTypeName())


fun Any?.asByteArray(): ByteArray {
    return if (this == null) ByteArray(1) { 0 }
    else {
        val byteWriter = CyberwayByteWriter()
        byteWriter.putByte(1)
        when (this) {
            is EosBool -> byteWriter.putByte(this.value)
            is EosAsset -> byteWriter.putAsset(this.value)
            is EosSymbolCode -> byteWriter.putByte(this.value)
            is EosSymbol -> byteWriter.putString(this.value)
            is CyberName -> byteWriter.putName(this.name)
            is Short -> byteWriter.putShort(this)
            is Int -> byteWriter.putInt(this)
            is Long -> byteWriter.putLong(this)
            is String -> byteWriter.putString(this)
            is ByteArray -> byteWriter.putBytes(this)
            is List<*> -> {
                if (this.firstOrNull() is String) byteWriter.putStringCollection(this as List<String>)
                else byteWriter.putByte(0)
            }
        }
        byteWriter.toBytes()
    }
}