import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.*
import io.golos.cyber4j.services.model.EventType
import io.golos.cyber4j.services.model.MobileShowSettings
import io.golos.cyber4j.services.model.NotificationSettings
import io.golos.cyber4j.services.model.ServiceSettingsLanguage
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

class NotifsTest {
    val cyber4j = Cyber4J(mainTestNetConfig)
    @Before
    fun before() {
        cyber4j.keyStorage.addAccountKeys(CyberName("yhlmjmqfqexl"),
                setOf(Pair(AuthType.ACTIVE, "5KcFFtzmeYeGt6HoWcZys282TnVVS2X1PdBgGzUsNXyRXdXsTcn")))
    }

    @Test
    fun tesGetNotifs() {
        val deviceId = UUID.randomUUID().toString()
        val fcmKey = UUID.randomUUID().toString()

        val subscriptionResult = cyber4j.subscribeOnMobilePushNotifications(deviceId, fcmKey)
        assertTrue(subscriptionResult is Either.Success)

        val unSubscriptionResult = cyber4j.unSubscribeOnNotifications(deviceId, fcmKey)
        assertTrue(unSubscriptionResult is Either.Success)

        val unreadCount = cyber4j.getFreshNotificationCount(cyber4j.resolveCanonicalCyberName("yhlmjmqfqexl".toCyberName()).name)
        assertTrue(unreadCount is Either.Success)
        assertTrue((unreadCount as Either.Success).value.fresh > -1)

        val marksAsREad = cyber4j.markAllEventsAsNotFresh()
        assertTrue(marksAsREad is Either.Success)

        val events = cyber4j.getEvents(cyber4j.resolveCanonicalCyberName("yhlmjmqfqexl".toCyberName()).name,
                null, 100, false, false, EventType.values().toList())
        assertTrue(events is Either.Success)
        assertTrue((events as Either.Success).value.data.isNotEmpty())
        assertTrue(events.value.fresh > -1)
        assertTrue(events.value.total > -1)

        val basicSettings = "{\"a\": 6}"
        val mobileSettings = MobileShowSettings(NotificationSettings(true, true, true, true, true, true,
                false, false, false, false, false, false, false), ServiceSettingsLanguage.RUSSIAN)

        val setSettingsResult = cyber4j.setUserSettings(deviceId, basicSettings, null, mobileSettings)
        assertTrue(setSettingsResult is Either.Success)

        val getSettingsResult = cyber4j.getUserSettings(deviceId)
        assertTrue(getSettingsResult is Either.Success)

        assertEquals(mobileSettings, (getSettingsResult as Either.Success).value.push)

        println( "unread = ${(cyber4j.getFreshNotificationCount("destroyer2k@golos") as Either.Success).value.fresh }")

    }
}