import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.*
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
        cyber4j.keyStorage.addAccountKeys(CyberName("destroyer2k@golos"),
                setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ")))
    }

    @Test
    fun tesGetNotifs() {
        val deviceId = UUID.randomUUID().toString()
        val fcmKey = UUID.randomUUID().toString()

        val subscriptionResult = cyber4j.subscribeOnMobilePushNotifications(deviceId, fcmKey)
        assertTrue(subscriptionResult is Either.Success)

        val unSubscriptionResult = cyber4j.unSubscribeOnNotifications(deviceId, fcmKey)
        assertTrue(unSubscriptionResult is Either.Success)

        val unreadCount = cyber4j.getUnreadCount(cyber4j.resolveCanonicalCyberName("destroyer2k".toCyberName()).name)
        //assertTrue(unreadCount is Either.Success)
        //assertTrue((unreadCount as Either.Success).value.fresh > -1)

        val marksAsREad = cyber4j.markAllEventsAsRead()
        assertTrue(marksAsREad is Either.Success)

        val events = cyber4j.getEvents(cyber4j.resolveCanonicalCyberName("destroyer2k".toCyberName()).name,
                null, 20, false, false, listOf(EventType.ALL))
        assertTrue(events is Either.Success)
        assertTrue((events as Either.Success).value.data.isNotEmpty())
        assertTrue(events.value.fresh > -1)
        assertTrue(events.value.total > 0)

        val basicSettings = "{\"a\": 6}"
        val mobileSettings = MobileShowSettings(NotificationSettings(true, true, true, true, true, true,
                false, false, false, false, false, false, false), ServiceSettingsLanguage.RUSSIAN)

        val setSettingsResult = cyber4j.setNotificationSettings(deviceId, basicSettings, null, mobileSettings)
        assertTrue(setSettingsResult is Either.Success)

        val getSettingsResult = cyber4j.getNotificationSettings(deviceId)
        assertTrue(getSettingsResult is Either.Success)

        assertEquals(mobileSettings, (getSettingsResult as Either.Success).value.push)

    }
}