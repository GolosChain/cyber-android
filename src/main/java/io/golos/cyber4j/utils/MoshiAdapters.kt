package io.golos.cyber4j.utils

import com.squareup.moshi.*
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

class EventTypeAdapter : JsonAdapter<EventType>() {
    override fun fromJson(reader: JsonReader): EventType? {
        reader.beginObject()
        val value = reader.nextString()
        return when (value) {
            "all" -> EventType.ALL
            "vote" -> EventType.VOTE
            "transfer" -> EventType.TRANSFER
            "reply" -> EventType.REPLY
            "flag" -> EventType.FLAG
            "subscribe" -> EventType.SUBSCRIBE
            "unsubscribe" -> EventType.UN_SUBSCRIBE
            "mention" -> EventType.MENTION
            "repost" -> EventType.REPOST
            "message" -> EventType.MESSAGE
            "reward" -> EventType.REWARD
            "curatorReward" -> EventType.CURATOR_REWARD
            "witnessVote" -> EventType.WITNESS_VOTE
            "witnessCancelVote" -> EventType.WITNESS_CANCEL_VOTE
            else -> throw java.lang.IllegalArgumentException("unknown type $value")
        }
    }

    override fun toJson(writer: JsonWriter, value: EventType?) {
        writer.value(value?.toString())
    }
}

class ServiceSettingsLanguageAdapter : JsonAdapter<ServiceSettingsLanguage>() {
    override fun fromJson(reader: JsonReader): ServiceSettingsLanguage? {
        val langString = reader.nextString()
        return when (langString) {
            "ru" -> ServiceSettingsLanguage.RUSSIAN
            "en" -> ServiceSettingsLanguage.ENGLISH
            else -> throw  java.lang.IllegalArgumentException("unknown language $langString")
        }
    }

    override fun toJson(writer: JsonWriter, value: ServiceSettingsLanguage?) {
        writer.value(value?.toString())
    }
}

class EventsAdapter {
    @FromJson
    fun fromJson(eventsJson: EventJson): Event = when (eventsJson.eventType) {
        EventType.ALL -> WitnessCancelVoteEvent(0, "event_all_serialized", false, emptyList(), 0L, 0L)
        EventType.VOTE -> VoteEvent(eventsJson.permlink!!,
                eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers, eventsJson.createdAt, eventsJson.updatedAt)
        EventType.FLAG -> FlagEvent(eventsJson.permlink!!,
                eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers, eventsJson.createdAt, eventsJson.updatedAt)
        EventType.TRANSFER -> TransferEvent(eventsJson.amount!!,
                eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers, eventsJson.createdAt, eventsJson.updatedAt)
        EventType.SUBSCRIBE -> SubscribeEvent(eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers.orEmpty(), eventsJson.createdAt, eventsJson.updatedAt)
        EventType.UN_SUBSCRIBE -> UnSubscribeEvent(eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers.orEmpty(), eventsJson.createdAt, eventsJson.updatedAt)
        EventType.REPLY -> ReplyEvent(eventsJson.permlink!!, eventsJson.parentPermlink!!,
                eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers, eventsJson.createdAt, eventsJson.updatedAt)
        EventType.MENTION -> MentionEvent(eventsJson.permlink!!, eventsJson.parentPermlink!!,
                eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers, eventsJson.createdAt, eventsJson.updatedAt)
        EventType.REPOST -> RepostEvent(eventsJson.permlink!!,
                eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers, eventsJson.createdAt, eventsJson.updatedAt)
        EventType.REWARD -> AwardEvent(eventsJson.reward!!, eventsJson.permlink!!,
                eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers, eventsJson.createdAt, eventsJson.updatedAt)
        EventType.CURATOR_REWARD -> CuratorAwardEvent(eventsJson.permlink!!, eventsJson.curatorTargetAuthor!!, eventsJson.curatorReward!!,
                eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers, eventsJson.createdAt, eventsJson.updatedAt)
        EventType.MESSAGE -> MessageEvent(eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers.orEmpty(), eventsJson.createdAt, eventsJson.updatedAt)
        EventType.WITNESS_VOTE -> WitnessVoteEvent(eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers.orEmpty(), eventsJson.createdAt, eventsJson.updatedAt)
        EventType.WITNESS_CANCEL_VOTE -> WitnessCancelVoteEvent(eventsJson.counter, eventsJson._id, eventsJson.fresh, eventsJson.fromUsers.orEmpty(), eventsJson.createdAt, eventsJson.updatedAt)
    }

    @ToJson
    fun toJson(event: Event): EventJson = when (event) {
        is WitnessCancelVoteEvent -> EventJson(EventType.WITNESS_CANCEL_VOTE,
                event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt)

        is VoteEvent -> EventJson(EventType.VOTE, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt,
                permlink = event.permlink)

        is FlagEvent -> EventJson(EventType.FLAG, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt,
                permlink = event.permlink)

        is TransferEvent -> EventJson(EventType.TRANSFER, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt,
                amount = event.amount)
        is SubscribeEvent -> EventJson(EventType.SUBSCRIBE, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt)

        is UnSubscribeEvent -> EventJson(EventType.UN_SUBSCRIBE, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt)

        is ReplyEvent -> EventJson(EventType.REPLY, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt,
                permlink = event.permlink, parentPermlink = event.parentPermlink)

        is MentionEvent -> EventJson(EventType.MENTION, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt,
                permlink = event.permlink, parentPermlink = event.parentPermlink)

        is RepostEvent -> EventJson(EventType.REPOST, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt,
                permlink = event.permlink)

        is AwardEvent -> EventJson(EventType.REWARD, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt,
                reward = event.reward, permlink = event.permlink)

        is CuratorAwardEvent -> EventJson(EventType.CURATOR_REWARD, event.counter, event._id, event.fresh,
                event.fromUsers, event.createdAt, event.updatedAt,
                permlink = event.permlink, curatorTargetAuthor = event.curatorTargetAuthor,
                curatorReward = event.curatorReward)

        is MessageEvent -> EventJson(EventType.MESSAGE, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt)

        is WitnessVoteEvent -> EventJson(EventType.WITNESS_VOTE, event.counter, event._id, event.fresh, event.fromUsers, event.createdAt, event.updatedAt)
    }
}