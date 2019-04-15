package io.golos.cyber4j.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import io.golos.cyber4j.model.*
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

class UserRegistrationStateAdapter : JsonAdapter<UserRegistrationState>() {

    override fun fromJson(reader: JsonReader): UserRegistrationState {
        val value = reader.nextString()

        return when (value) {
            "registered" -> UserRegistrationState.REGISTERED
            "firstStep" -> UserRegistrationState.FIRST_STEP
            "verify" -> UserRegistrationState.VERIFY
            "setUsername" -> UserRegistrationState.SET_USER_NAME
            "toBlockChain" -> UserRegistrationState.TO_BLOCK_CHAIN
            else -> throw IllegalArgumentException("unknown step $value")
        }
    }

    override fun toJson(writer: JsonWriter, value: UserRegistrationState?) {
        writer.value(value?.name ?: "")
    }
}

class UserRegistrationStrategyAdapter : JsonAdapter<RegistrationStrategy>() {

    override fun fromJson(reader: JsonReader): RegistrationStrategy {
        val value = reader.nextString()

        return when (value) {
            "smsFromUser" -> RegistrationStrategy.SMS_FROM_USER
            "smsToUser" -> RegistrationStrategy.SMS_TO_USER
            "mail" -> RegistrationStrategy.MAIL
            "social" -> RegistrationStrategy.SOCIAL
            else -> throw IllegalArgumentException("unknown strategy $value")
        }
    }

    override fun toJson(writer: JsonWriter, value: RegistrationStrategy?) {
        writer.value(value?.name ?: "")
    }
}

class ContentRowAdapter : JsonAdapter<ContentRow>() {
    override fun fromJson(reader: JsonReader): ContentRow? {
        reader.beginObject()
        var type: String? = null
        var src: String? = null
        var content: String? = null

        while (reader.hasNext()) {
            val fieldName = reader.nextName()
            when (fieldName) {
                "type" -> type = reader.nextString()
                "content" -> content = reader.nextString()
                "src" -> src = reader.nextString()
                else -> reader.nextString()
            }
        }

        val out = when (type!!) {
            TextRow.typeName -> TextRow(content!!)
            ImageRow.typeName -> ImageRow(src!!)
            else -> throw java.lang.IllegalArgumentException("unknown type $type")
        }
        reader.endObject()
        return out
    }

    override fun toJson(writer: JsonWriter, value: ContentRow?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}