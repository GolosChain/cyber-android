import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.*
import io.golos.cyber4j.utils.*
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class Utils {
    private val privateTestNetClient = Cyber4J(mainTestNetConfig)
    private lateinit var secondAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        privateTestNetClient.keyStorage.addAccountKeys(
            testInMainTestNetAccount.first,
            setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second))
        )
        secondAccount = testInMainTestNetAccountSecond
    }

    @Test
    fun cyberNameTest() {
        val cyber4j = privateTestNetClient
        val posts = (cyber4j.getUserPosts(
            testInMainTestNetAccount.first,
            ContentParsingType.MOBILE,
            100,
            DiscussionTimeSort.SEQUENTIALLY
        ) as Either.Success)
        val firstPost = posts.value.items.first()
        val second = posts.value.items[1]

        cyber4j.vote(
            firstPost.contentId.userId.toCyberName(), firstPost.contentId.permlink, firstPost.contentId.refBlockNum,
            (Math.random() * 10_000).toShort()
        ) as Either.Success
        cyber4j.vote(
            firstPost.contentId.userId.toCyberName(), firstPost.contentId.permlink, firstPost.contentId.refBlockNum,
            (Math.random() * -10_000).toShort()
        ) as Either.Success

        cyber4j.vote(
            second.contentId.userId.toCyberName(), second.contentId.permlink, second.contentId.refBlockNum,
            (Math.random() * 10_000).toShort()
        ) as Either.Success
        cyber4j.vote(
            second.contentId.userId.toCyberName(), second.contentId.permlink, second.contentId.refBlockNum,
            (Math.random() * -10_000).toShort()
        ) as Either.Success
    }

    @Test
    fun testParralel() {
        val exec = Executors.newFixedThreadPool(15)
        val clock = AtomicInteger(0)

        val numOfCommands = 25

        val latch = CountDownLatch(numOfCommands)

        (0 until numOfCommands).forEach {
            exec.execute {
                try {
                    expensiveTask()
                } finally {
                    latch.countDown()
                    clock.incrementAndGet()
                }
            }
        }
        latch.await()
        assertEquals(numOfCommands, clock.get())

    }

    private fun expensiveTask() {
        Thread.sleep(5_000)
        val rnd = Math.random()
        if (rnd > 0.49) throw IllegalStateException("just exception")
    }

    @Test
    fun createUserTest() {
        println(AuthUtils.generatePrivateWiFs("sename", "gcabvqhrsdxx", AuthType.values()))
    }

    @Test
    fun createPosts() {
        (0..10).forEach {
            privateTestNetClient.createPost(
                "title", "body $it", emptyList(), DiscussionCreateMetadata(
                    emptyList(),
                    emptyList()
                ), null
            )
            Thread.sleep(300)
        }
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
}