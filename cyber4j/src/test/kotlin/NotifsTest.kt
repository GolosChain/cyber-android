import io.golos.cyber4j.services.model.EventType
import io.golos.cyber4j.services.model.MobileShowSettings
import io.golos.cyber4j.services.model.NotificationSettings
import io.golos.cyber4j.services.model.ServiceSettingsLanguage
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.StringSigner
import io.golos.sharedmodel.CyberName
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

class NotifsTest {
    val client = getClient()
    val userId = CyberName("tst1fddizlpd")
    val key = "5KPsQEAtq9xVgeUSqH5eQnMN8ih3yuiHL63md6GNFH4iqj2bDLP"

    @Before
    fun before() {
        client.unAuth()
    }

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
        assertTrue((events as Either.Success).value.data.isNotEmpty())
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