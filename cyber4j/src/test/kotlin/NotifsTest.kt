import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.services.model.EventType
import io.golos.cyber4j.services.model.MobileShowSettings
import io.golos.cyber4j.services.model.NotificationSettings
import io.golos.cyber4j.services.model.ServiceSettingsLanguage
import io.golos.sharedmodel.Either
import io.golos.cyber4j.utils.StringSigner
import io.golos.sharedmodel.CyberName
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

class NotifsTest {
    val client = Cyber4J()

    val userId = (client.resolveCanonicalCyberName("sjsjshsjjssjsjs", "gls") as Either.Success).value.userId
    val key = "5JyoxZSTfy3Jv7XqvSZmBiUxzehHM14oBbkeDgpdG5URQ72j3RB"

    @Test
    fun tesGetNotifs() {
        val secret = client.getAuthSecret().getOrThrow()
        client.authWithSecret(userId.name,
                secret.secret,
                StringSigner.signString(secret.secret, key)).getOrThrow()

        assertTrue(client.isUserAuthed().getOrThrow())

        val deviceId = UUID.randomUUID().toString()
        val fcmKey = UUID.randomUUID().toString()

        val subscriptionResult = client.subscribeOnMobilePushNotifications(deviceId, fcmKey)
        assertTrue(subscriptionResult is Either.Success)

        val unSubscriptionResult = client.unSubscribeOnNotifications(deviceId, fcmKey)
        assertTrue(unSubscriptionResult is Either.Success)

        val unreadCount = client.getFreshNotificationCount(userId.name)
        assertTrue(unreadCount is Either.Success)
        assertTrue((unreadCount as Either.Success).value.fresh > -1)

        val marksAsREad = client.markAllEventsAsNotFresh()
        assertTrue(marksAsREad is Either.Success)

        val events = client.getEvents(userId.name,
                null, 100, false, false, EventType.values().toList())
        assertTrue(events is Either.Success)
        assertTrue((events as Either.Success).value.data != null)
        assertTrue(events.value.fresh > -1)
        assertTrue(events.value.total > -1)

        val basicSettings = "{\"a\": 6}"
        val mobileSettings = MobileShowSettings(NotificationSettings(true, true, true, true, true, true,
                false, false, false, false, false, false, false), ServiceSettingsLanguage.RUSSIAN)

        val setSettingsResult = client.setUserSettings(deviceId, basicSettings, null, mobileSettings)
        assertTrue(setSettingsResult is Either.Success)

        val getSettingsResult = client.getUserSettings(deviceId)
        assertTrue(getSettingsResult is Either.Success)

        assertEquals(mobileSettings, (getSettingsResult as Either.Success).value.push)

    }
}