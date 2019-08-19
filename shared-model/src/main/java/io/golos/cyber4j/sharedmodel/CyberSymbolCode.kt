package io.golos.cyber4j.sharedmodel

class CyberSymbol(precision: Byte,
                  name: String,
                  val symbol: String = "") {

    @Transient
    val symbolCode: ByteArray = ByteArray(8)

    init {
        if (symbol.isNotEmpty()) {
            val splited = symbol.split(",")
            symbolCode[0] = splited[0].toByte()
            (0 until 7).forEach {
                symbolCode[it + 1] = splited[1].getOrElse(it) { 0.toChar() }.toByte()
            }
        } else {
            symbolCode[0] = precision
            val nameBytes = name.toByteArray()
            (0 until 7).forEach {
                symbolCode[it + 1] = nameBytes.getOrElse(it) { 0 }
            }
        }
    }
}