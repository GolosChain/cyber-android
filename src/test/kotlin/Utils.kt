import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.model.DiscussionCreateMetadata
import io.golos.cyber4j.model.DiscussionTimeSort
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import org.junit.Before
import org.junit.Test

class Utils {
    private val privateTestNetClient = Cyber4J(mainTestNetConfig)
    private lateinit var secondAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        privateTestNetClient.keyStorage.addAccountKeys(testInMainTestNetAccount.first,
                setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)))
        secondAccount = testInMainTestNetAccountSecond
    }

    @Test
    fun cyberNameTest() {
        val cyber4j = privateTestNetClient
        val posts = (cyber4j.getUserPosts(testInMainTestNetAccount.first, 100, DiscussionTimeSort.SEQUENTIALLY) as Either.Success)
        val firstPost = posts.value.items.first()
        val second = posts.value.items[1]

        cyber4j.vote(firstPost.contentId.userId.toCyberName(), firstPost.contentId.permlink, firstPost.contentId.refBlockNum,
                (Math.random() * 10_000).toShort()) as Either.Success
        cyber4j.vote(firstPost.contentId.userId.toCyberName(), firstPost.contentId.permlink, firstPost.contentId.refBlockNum,
                (Math.random() * -10_000).toShort()) as Either.Success

        cyber4j.vote(second.contentId.userId.toCyberName(), second.contentId.permlink, second.contentId.refBlockNum,
                (Math.random() * 10_000).toShort()) as Either.Success
        cyber4j.vote(second.contentId.userId.toCyberName(), second.contentId.permlink, second.contentId.refBlockNum,
                (Math.random() * -10_000).toShort()) as Either.Success
    }

    @Test
    fun createPosts(){
        (0..10).forEach {
            privateTestNetClient.createPost("title", "body $it", emptyList(), DiscussionCreateMetadata(emptyList(),
                    emptyList()), null)
            Thread.sleep(300)
        }
    }

}