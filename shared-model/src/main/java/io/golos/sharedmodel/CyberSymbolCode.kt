package io.golos.sharedmodel

data class CyberSymbolCode(val precision: Byte,
                           val name: String) {
    val symbolCode: ByteArray = ByteArray(8)

    init {
        symbolCode[0] = precision
        val nameBytes = name.toByteArray()
        (0 until 7).forEach {
            symbolCode[it + 1] = nameBytes.getOrElse(it) { 0 }
        }
    }
}