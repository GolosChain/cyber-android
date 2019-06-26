package io.golos.sharedmodel

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class CyberNameAdapter : JsonAdapter<CyberName>() {

    override fun fromJson(reader: JsonReader): CyberName? {
        val nextToken = reader.peek()
        return if (nextToken == JsonReader.Token.STRING) {
            val value = reader.nextString()
            CyberName(value)
        } else {
            reader.beginObject()
            reader.nextName()
            val out = CyberName(reader.nextString())
            reader.endObject()
            out
        }
    }

    override fun toJson(writer: JsonWriter, value: CyberName?) {
        writer.value(value?.name)
    }
}

class CyberAssetAdapter : JsonAdapter<CyberAsset>() {

    override fun fromJson(reader: JsonReader): CyberAsset? {
        val nextToken = reader.peek()
        return if (nextToken == JsonReader.Token.STRING) {
            val value = reader.nextString()
            CyberAsset(value)
        } else {
            val token = reader.peek()
            if (token == JsonReader.Token.NULL) {
                return reader.nextNull<CyberAsset>()
            }

            reader.beginObject()
            reader.nextName()
            val out = CyberAsset(reader.nextString())
            reader.endObject()
            out
        }
    }

    override fun toJson(writer: JsonWriter, value: CyberAsset?) {
        writer.value(value?.amount)
    }
}

class CyberSymbolAdapter : JsonAdapter<CyberSymbol>() {

    override fun fromJson(reader: JsonReader): CyberSymbol? {
        val nextToken = reader.peek()
        return if (nextToken == JsonReader.Token.STRING) {
            val value = reader.nextString()
            CyberSymbol(0, "", value)
        } else {
            reader.beginObject()
            reader.nextName()
            val out = CyberSymbol(0, "", reader.nextString())
            reader.endObject()
            out
        }
    }

    override fun toJson(writer: JsonWriter, value: CyberSymbol?) {
        writer.value(value?.symbol)
    }
}