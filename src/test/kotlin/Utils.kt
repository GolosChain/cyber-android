import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.Cyber4JCoroutinesAdapter
import io.golos.cyber4j.model.ContentRow
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.services.model.*
import io.golos.cyber4j.utils.*
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*

class Utils {
    private lateinit var client: Cyber4J
    private lateinit var secondAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        client = getClient()
        secondAccount = account(client.config.toConfigType(), false)
    }

    @Test
    fun cyberNameTest() {
        val cyber4j = client
        val posts = (cyber4j.getUserPosts(
                client.activeAccountPair.first,
                ContentParsingType.MOBILE,
                100,
                DiscussionTimeSort.SEQUENTIALLY
        ) as Either.Success)
        val firstPost = posts.value.items.first()
        val second = posts.value.items[1]

        cyber4j.vote(
                firstPost.contentId.userId.toCyberName(), firstPost.contentId.permlink,
                (Math.random() * 10_000).toShort()
        ) as Either.Success
        cyber4j.vote(
                firstPost.contentId.userId.toCyberName(), firstPost.contentId.permlink,
                (Math.random() * -10_000).toShort()
        ) as Either.Success

        cyber4j.vote(
                second.contentId.userId.toCyberName(), second.contentId.permlink,
                (Math.random() * 10_000).toShort()
        ) as Either.Success
        cyber4j.vote(
                second.contentId.userId.toCyberName(), second.contentId.permlink,
                (Math.random() * -10_000).toShort()
        ) as Either.Success
    }


    @Test
    fun deserializeEvents() {
        val eventsString = (Cyber4J::class.java).getResource("/test.json").readText(Charset.defaultCharset())

        assertNotNull(eventsString)

        val moshi = Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter())
                .add(BigInteger::class.java, BigIntegerAdapter())
                .add(CyberName::class.java, CyberNameAdapter())
                .add(UserRegistrationState::class.java, UserRegistrationStateAdapter())
                .add(RegistrationStrategy::class.java, UserRegistrationStrategyAdapter())
                .add(ContentRow::class.java, ContentRowAdapter())
                .add(EventType::class.java, EventTypeAdapter())
                .add(CyberNameAdapter())
                .add(ServiceSettingsLanguage::class.java, ServiceSettingsLanguageAdapter())
                .add(EventsAdapter())
                .build()

        val events = moshi.adapter<EventsData>(EventsData::class.java).fromJson(eventsString)
        assertNotNull(events)

        assertTrue(events!!.data.isNotEmpty())
    }

    @Test
    fun imageUploadTest() {
        val img = File((Cyber4J::class.java).getResource("/test2.jpg").file!!)
        assertNotNull(img)
        val uploadResponse = client.uploadImage(img)
        assertTrue(uploadResponse is Either.Success)
        println((uploadResponse as Either.Success).value)

    }

    @Test
    fun coroutinesTest() {
        runBlocking {
            val adapter = Cyber4JCoroutinesAdapter(client)
            (0..9).forEach {
                async {
                    adapter.getCommunityPosts("gls", ContentParsingType.RAW, 1, DiscussionTimeSort.INVERTED, "")
                    println(it)
                }
            }
        }
    }

    @Test
    fun withdrawtest() {
        val result = client.withdraw(client.keyStorage.getActiveAccount(),
                secondAccount.first, "0.001000 GOLOS", client.keyStorage.activeAccountPair.second)
        assertTrue(result is Either.Success)

        val stopResult =
                client.stopWithdraw(client.keyStorage.getActiveAccount(), client.keyStorage.activeAccountPair.second)
        assertTrue(stopResult is Either.Success)
    }

    @Test
    fun delegate() {
        val result = client.delegate(
                client.keyStorage.getActiveAccount(),
                secondAccount.first, "1.000000 GOLOS", 100.toShort(),
                1.toByte(),
                client.keyStorage.activeAccountPair.second
        )
        assertTrue(result is Either.Success)

    }

    @Test
    fun testResolveName() {
        val usernameDomain = "lakin-ernesto-phd"
        val resolvedCanonicalName = client.resolveCanonicalCyberName(usernameDomain,
                "cyber").getOrThrow()
        assertNotNull(resolvedCanonicalName.username)
        assertNotNull(resolvedCanonicalName.userId)
        assertTrue(resolvedCanonicalName.userId.name != usernameDomain)
    }
}