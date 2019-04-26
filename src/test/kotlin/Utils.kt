import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.*
import io.golos.cyber4j.utils.*
import junit.framework.Assert.*
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class Utils {
    private val privateTestNetClient = Cyber4J(mainTestNetConfig.copy(performAutoAuthOnActiveUserSet = false))
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

        val numOfCommands = 2

        val latch = CountDownLatch(numOfCommands)
        val client = Cyber4J(
            mainTestNetConfig.copy(performAutoAuthOnActiveUserSet = false,
                httpLogger = HttpLoggingInterceptor.Logger {
                    println("thread ${Thread.currentThread()}\n$it")
                })
        )
        client.keyStorage.addAccountKeys(
            testInMainTestNetAccount.first,
            setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second))
        )

        (0 until numOfCommands).forEach {
            exec.execute {
                try {
                    expensiveTask(client)
                } finally {
                    latch.countDown()
                    clock.incrementAndGet()
                }
            }
        }
        latch.await()
        assertEquals(numOfCommands, clock.get())

    }

    private fun expensiveTask(client: Cyber4J) {
        val rnd = Math.random()
        if (rnd > 0.49) client.createPost(
            "title ${Thread.currentThread()}",
            "body", emptyList(), DiscussionCreateMetadata(emptyList(), emptyList()), null
        )
        else client.deleteUserMetadata()
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

    @Test
    fun imageUploadTest() {
        val img = File((Cyber4J::class.java).getResource("/test2.jpg").file!!)
        assertNotNull(img)
        val uploadResponse = privateTestNetClient.uploadImage(img)
        assertTrue(uploadResponse is Either.Success)
        println((uploadResponse as Either.Success).value)

    }

    @Test
    fun testResolveName() {
        val usernameCanonical = "xlvgwhfbffoo"
        val usernameDomain = "destroyer2k@golos".toCyberName()
        val resolvedCanonicalName = privateTestNetClient.resolveCanonicalCyberName(usernameCanonical.toCyberName())
        assertEquals(usernameCanonical, resolvedCanonicalName.name)
        assertTrue(resolvedCanonicalName.isCanonicalName)

        assertTrue(!usernameDomain.isCanonicalName)
        val resolvedDomainName = privateTestNetClient.resolveCanonicalCyberName(usernameDomain)

        assertTrue(resolvedDomainName.isCanonicalName)
        assertNotSame(usernameDomain.name, resolvedDomainName.name)
        assertEquals(usernameDomain.name, resolvedDomainName.domainName)
        println("resolvedCanonicalName = $resolvedCanonicalName")
        println("resolvedDomainName = $resolvedDomainName")
    }
}