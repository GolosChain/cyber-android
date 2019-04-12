import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.model.DiscussionCreateMetadata
import io.golos.cyber4j.model.Tag
import io.golos.cyber4j.utils.AuthUtils
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.nio.charset.Charset
import java.util.*

class PostingTest {
    private val client = Cyber4J(mainTestNetConfig)
    private lateinit var secondAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        client.keyStorage.addAccountKeys(testInMainTestNetAccount.first,
                setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)))
        secondAccount = secondAccount
    }

    val testMetadata = DiscussionCreateMetadata(listOf(DiscussionCreateMetadata.EmbedmentsUrl("test_url")), listOf("тээст"))

    @Test
    fun testPostOnPrivateTestNet() {

        val postResponse = client.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata, createRandomCurationReward())

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val commentCreationResult = client.createComment("тестовый коммент",
                postResult.message_id.author, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                listOf(), testMetadata, createRandomCurationReward())

        assertTrue("comment creation fail on test net", commentCreationResult is Either.Success)


        val secondPostResponse = client.createPost(secondAccount.first, secondAccount.second, "тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata, createRandomCurationReward())
        assertTrue("post creation fail on test net", secondPostResponse is Either.Success)

        val secondPostResult = (secondPostResponse as Either.Success).value.extractResult()

        val secondCommentCreationResult = client.createComment(secondAccount.first, secondAccount.second,
                "тестовый коммент",
                secondPostResult.message_id.author, secondPostResult.message_id.permlink, secondPostResult.message_id.ref_block_num,
                listOf(), testMetadata, createRandomCurationReward())

        assertTrue("comment creation fail on test net", secondCommentCreationResult is Either.Success)

    }

    @Test
    fun updatePostTest() {

        val postResponse = client.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata, createRandomCurationReward())

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val updateResponse = client.updatePost(postResult.message_id.permlink, postResult.message_id.ref_block_num,
                "new title", "new body", listOf(Tag("test")), testMetadata)

        assertTrue("post update fail on test net", updateResponse is Either.Success)
        val updateResult = (updateResponse as Either.Success).value.extractResult()
        assertEquals("title was not updated", "new title", updateResult.headermssg)
        assertEquals("body was not updated", "new body", updateResult.bodymssg)

        Thread.sleep(1_000)

        val updateResponseSecond = client.updatePost(testInMainTestNetAccount.second,
                testInMainTestNetAccount.first, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                "new title1", "new body1", listOf(Tag("test")), testMetadata)

        assertTrue("post update fail on test net", updateResponseSecond is Either.Success)
        val updateResultSecond = (updateResponseSecond as Either.Success).value.extractResult()
        assertEquals("title was not updated", "new title1", updateResultSecond.headermssg)
        assertEquals("body was not updated", "new body1", updateResultSecond.bodymssg)

        val deleteReponse = client.deletePostOrComment(updateResultSecond.message_id.permlink, updateResultSecond.message_id.ref_block_num)
        assertTrue("post deleteReponse fail on test net", deleteReponse is Either.Success)

    }

    @Test
    fun updateComment() {
        val postResponse = client.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata, createRandomCurationReward())

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val commentCreationResponse = client.createComment("тестовый коммент",
                postResult.message_id.author, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                listOf(), testMetadata, createRandomCurationReward())

        assertTrue("comment creation fail on test net", commentCreationResponse is Either.Success)

        val commentCreationResult = (commentCreationResponse as Either.Success).value.extractResult()


        val updateResponse = client.updateComment(commentCreationResult.message_id.permlink, commentCreationResult.message_id.ref_block_num,
                "new body", listOf(Tag("test")), testMetadata)

        assertTrue("comment update fail on test net", updateResponse is Either.Success)
        val updateResult = (updateResponse as Either.Success).value.extractResult()

        assertEquals("body was not updated", "new body", updateResult.bodymssg)

        Thread.sleep(1_000)

        val updateResponseSecond = client.updateComment(testInMainTestNetAccount.second,
                testInMainTestNetAccount.first, commentCreationResult.message_id.permlink, commentCreationResult.message_id.ref_block_num,
                "new body1", listOf(Tag("test")), testMetadata)

        assertTrue("comment update fail on test net", updateResponseSecond is Either.Success)
        val updateResultSecond = (updateResponseSecond as Either.Success).value.extractResult()
        assertEquals("body was not updated", "new body1", updateResultSecond.bodymssg)


    }

    @Test
    fun deleteTest() {

        val postResponse = client.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata, createRandomCurationReward())

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val deleteReponse = client.deletePostOrComment(postResult.message_id.permlink, postResult.message_id.ref_block_num)
        assertTrue("comment deleteReponse fail on test net", deleteReponse is Either.Success)

        val postResponseSecond = client.createPost(secondAccount.first, secondAccount.second,
                "тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata, createRandomCurationReward())

        assertTrue("post creation fail on test net", postResponseSecond is Either.Success)

        val postResultSecond = (postResponseSecond as Either.Success).value.extractResult()

        val deleteReponseSecond = client.deletePostOrComment(secondAccount.second,
                secondAccount.first,
                postResultSecond.message_id.permlink,
                postResultSecond.message_id.ref_block_num)
        assertTrue("post deleteReponse fail on test net", deleteReponseSecond is Either.Success)
    }

    @Test
    fun testReblog() {
        val postResponse = client.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata, createRandomCurationReward())

        val postMessageId = (postResponse as Either.Success).value.extractResult().message_id

        val reblogResult = client.reblog(postMessageId.author, postMessageId.permlink, postMessageId.ref_block_num)

        assertTrue(reblogResult is Either.Success)

        println(reblogResult)

    }

    private fun createRandomCurationReward(): Short? {
        val random = Math.random()
        return if (random < 0.1 || random > 0.8) null
        else (random * 10_000).toShort()
    }
}