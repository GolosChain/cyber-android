import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.golos.abi.implementation.publish.CreatemssgPublishAction
import io.golos.abi.implementation.publish.CreatemssgPublishStruct
import io.golos.abi.implementation.publish.MssgidPublishStruct
import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.Cyber4JCoroutinesAdapter
import io.golos.cyber4j.model.ContentRow
import io.golos.cyber4j.services.model.*
import io.golos.cyber4j.utils.*
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberNameAdapter
import io.golos.sharedmodel.Either
import io.golos.sharedmodel.GolosEosError
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
        //  client = getClient()
        //secondAccount = account(client.config.toConfigType(), true)
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
        val eventsString = (DomainTest::class.java).getResource("/test.json").readText(Charset.defaultCharset())

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
        val img = File((DomainTest::class.java).getResource("/test2.jpg").file!!)
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

    @Test
    fun testRawApi() {

        val result: Either<out TransactionCommitted<out CreatemssgPublishStruct>, GolosEosError> = CreatemssgPublishAction(CreatemssgPublishStruct(
                MssgidPublishStruct(secondAccount.first,
                        "sdgewtwesgsd2322222511"),
                MssgidPublishStruct(CyberName(""), ""),
                emptyList(), 0, true, "title", " nывпывп 25 ывп ывп",
                "ru", emptyList(), "", null, null)
        ).push(
                listOf(TransactionAuthorizationAbi(secondAccount.first.name, "active")),
                EosPrivateKey(secondAccount.second)
        )

        print(result)

        assertTrue(result is Either.Success)
    }
}