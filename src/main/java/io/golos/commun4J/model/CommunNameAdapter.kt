package io.golos.commun4J.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader

class CommunNameAdapter {
    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<CommunName>): CommunName? {
        val value = jsonReader.nextString()
        return CommunName(value)
    }
}