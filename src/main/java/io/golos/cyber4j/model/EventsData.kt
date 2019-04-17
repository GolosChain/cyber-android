package io.golos.cyber4j.model


class EventsData(val total: Int,
                 val fresh: Int,
                 val data: List<Event>)

sealed class Event(
        val eventType: EventType,
        val counter: Int,
        val _id: String,
        val fresh: Boolean,
        val fromUsers: List<String>,
        val createdAt: Long,
        val updatedAt: Long)

class VoteEvent(val permlink: String,
                counter: Int,
                _id: String,
                fresh: Boolean,
                fromUsers: List<String>,
                createdAt: Long,
                updatedAt: Long) : Event(EventType.FLAG, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class FlagEvent(val permlink: String,
                counter: Int,
                _id: String,
                fresh: Boolean,
                fromUsers: List<String>,
                createdAt: Long,
                updatedAt: Long) : Event(EventType.VOTE, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class TransferEvent(val amount: String,
                    counter: Int,
                    _id: String,
                    fresh: Boolean,
                    fromUsers: List<String>,
                    createdAt: Long,
                    updatedAt: Long) : Event(EventType.TRANSFER, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class SubscribeEvent(counter: Int,
                     _id: String,
                     fresh: Boolean,
                     fromUsers: List<String>,
                     createdAt: Long,
                     updatedAt: Long) : Event(EventType.SUBSCRIBE, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class UnSubscribeEvent(counter: Int,
                       _id: String,
                       fresh: Boolean,
                       fromUsers: List<String>,
                       createdAt: Long,
                       updatedAt: Long) : Event(EventType.UN_SUBSCRIBE, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class ReplyEvent(val permlink: String,
                 val parentPermlink: String,
                 counter: Int,
                 _id: String,
                 fresh: Boolean,
                 fromUsers: List<String>,
                 createdAt: Long,
                 updatedAt: Long) : Event(EventType.REPLY, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class MentionEvent(val permlink: String,
                   val parentPermlink: String,
                   counter: Int,
                   _id: String,
                   fresh: Boolean,
                   fromUsers: List<String>,
                   createdAt: Long,
                   updatedAt: Long) : Event(EventType.MENTION, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class RepostEvent(val permlink: String,
                  counter: Int,
                  _id: String,
                  fresh: Boolean,
                  fromUsers: List<String>,
                  createdAt: Long,
                  updatedAt: Long) : Event(EventType.REPOST, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class AwardEvent(val reward: Award,
                 val permlink: String,
                 counter: Int,
                 _id: String,
                 fresh: Boolean,
                 fromUsers: List<String>,
                 createdAt: Long,
                 updatedAt: Long)
    : Event(EventType.REWARD, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class CuratorAwardEvent(val permlink: String,
                        val curatorTargetAuthor: String,
                        val curatorReward: Double,
                        counter: Int,
                        _id: String,
                        fresh: Boolean,
                        fromUsers: List<String>,
                        createdAt: Long,
                        updatedAt: Long)
    : Event(EventType.CURATOR_REWARD, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class MessageEvent(counter: Int,
                   _id: String,
                   fresh: Boolean,
                   fromUsers: List<String>,
                   createdAt: Long,
                   updatedAt: Long) : Event(EventType.MESSAGE, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class WitnessVoteEvent(counter: Int,
                       _id: String,
                       fresh: Boolean,
                       fromUsers: List<String>,
                       createdAt: Long,
                       updatedAt: Long) : Event(EventType.WITNESS_VOTE, counter, _id, fresh, fromUsers, createdAt, updatedAt)

class WitnessCancelVoteEvent(counter: Int,
                             _id: String,
                             fresh: Boolean,
                             fromUsers: List<String>,
                             createdAt: Long,
                             updatedAt: Long) : Event(EventType.WITNESS_CANCEL_VOTE, counter, _id, fresh, fromUsers, createdAt, updatedAt)


class Award(val golos: Double,
            val golosPower: Double,
            val gbg: Double)

class EventJson(
        val eventType: EventType,
        val counter: Int,
        val _id: String,
        val fresh: Boolean,
        val fromUsers: List<String>,
        val createdAt: Long,
        val updatedAt: Long,
        val permlink: String? = null,
        val amount: String? = null,
        val parentPermlink: String? = null,
        val reward: Award? = null,
        val curatorTargetAuthor: String? = null,
        val curatorReward: Double? = null)