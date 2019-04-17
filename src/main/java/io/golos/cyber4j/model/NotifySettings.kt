package io.golos.cyber4j.model

data class WebShowSettings(
        val show: NotificationSettings?)

data class MobileShowSettings(val show: NotificationSettings?,
                              val lang: ServiceSettingsLanguage?)

data class NotificationSettings(val upvote: Boolean?,
                                val downvote: Boolean?,
                                val reply: Boolean?,
                                val transfer: Boolean?,
                                val subscribe: Boolean?,
                                val unsubscribe: Boolean?,
                                val mention: Boolean?,
                                val repost: Boolean?,
                                val message: Boolean?,
                                val witnessVote: Boolean?,
                                val witnessCancelVote: Boolean?,
                                val reward: Boolean?,
                                val curatorReward: Boolean?)

enum class ServiceSettingsLanguage {
    ENGLISH, RUSSIAN;

    override fun toString(): String {
        return when (this) {
            ENGLISH -> "en"
            RUSSIAN -> "ru"
        }
    }
}


class NotifySettings(
        //device id
        val profile: String,
        val basic: Any?,
        val notify: WebShowSettings?,
        val push: MobileShowSettings?
)

internal class ServicesSettingsRequest(
        val profile: String)