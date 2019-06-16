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