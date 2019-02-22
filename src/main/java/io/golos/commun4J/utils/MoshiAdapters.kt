package io.golos.commun4J.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import io.golos.commun4J.model.CommunName
import java.math.BigInteger


class CommunNameAdapter : JsonAdapter<CommunName>() {

    override fun fromJson(reader: JsonReader): CommunName? {
        val value = reader.nextString()
        return CommunName(value)
    }

    override fun toJson(writer: JsonWriter, value: CommunName?) {
        writer.value(value?.name)
    }
    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<CommunName>): CommunName? {
        val value = jsonReader.nextString()
        return CommunName(value)
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