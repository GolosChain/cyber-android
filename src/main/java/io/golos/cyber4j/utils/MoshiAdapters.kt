package io.golos.cyber4j.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import io.golos.cyber4j.model.CyberName
import java.math.BigInteger


class CyberNameAdapter : JsonAdapter<CyberName>() {

    override fun fromJson(reader: JsonReader): CyberName? {
        val value = reader.nextString()
        return CyberName(value)
    }

    override fun toJson(writer: JsonWriter, value: CyberName?) {
        writer.value(value?.name)
    }
    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<CyberName>): CyberName? {
        val value = jsonReader.nextString()
        return CyberName(value)
    }
}

class BigIntegerAdapter : JsonAdapter<BigInteger>() {

    override fun fromJson(reader: JsonReader): BigInteger? {
        val value = reader.nextString()
        return BigInteger(value)
    }

    override fun toJson(writer: JsonWriter, value: BigInteger?) {
        writer.value(value?.toString() ?: "")
    }
}