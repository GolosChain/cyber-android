import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.*
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

class PostingTest {
    private val client = Cyber4J(mainTestNetConfig)
    private lateinit var secondAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        client.keyStorage.addAccountKeys(testInMainTestNetAccount.first,
                setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)))
        secondAccount = testInMainTestNetAccountSecond
    }

    val testMetadata = DiscussionCreateMetadata(listOf(DiscussionCreateMetadata.EmbedmentsUrl("test_url")))

    @Test
    fun testPostOnPrivateTestNet() {

        val postResponse = client.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata)

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val commentCreationResult = client.createComment("тестовый коммент",
                postResult.message_id.author, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                Tag("test"), testMetadata)

        assertTrue("comment creation fail on test net", commentCreationResult is Either.Success)


        val secondPostResponse = client.createPost(secondAccount.first, secondAccount.second, "тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata)
        assertTrue("post creation fail on test net", secondPostResponse is Either.Success)

        val secondPostResult = (secondPostResponse as Either.Success).value.extractResult()

        val secondCommentCreationResult = client.createComment(secondAccount.first, secondAccount.second,
                "тестовый коммент",
                secondPostResult.message_id.author, secondPostResult.message_id.permlink, secondPostResult.message_id.ref_block_num,
                Tag("test"), testMetadata)

        assertTrue("comment creation fail on test net", secondCommentCreationResult is Either.Success)

    }

    @Test
    fun updatePostTest() {

        val postResponse = client.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata)

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val updateResponse = client.updatePost(postResult.message_id.permlink, postResult.message_id.ref_block_num,
                "new title", "new body", listOf(Tag("test")))

        assertTrue("post update fail on test net", updateResponse is Either.Success)
        val updateResult = (updateResponse as Either.Success).value.extractResult()
        assertEquals("title was not updated", "new title", updateResult.headermssg)
        assertEquals("body was not updated", "new body", updateResult.bodymssg)

        Thread.sleep(1_000)

        val updateResponseSecond = client.updatePost(testInMainTestNetAccount.second,
                testInMainTestNetAccount.first, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                "new title1", "new body1", listOf(Tag("test")))

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
                "тестовое тело поста", listOf(Tag("test")), testMetadata)

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val commentCreationResponse = client.createComment("тестовый коммент",
                postResult.message_id.author, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                Tag("test"), testMetadata)

        assertTrue("comment creation fail on test net", commentCreationResponse is Either.Success)

        val commentCreationResult = (commentCreationResponse as Either.Success).value.extractResult()


        val updateResponse = client.updateComment(commentCreationResult.message_id.permlink, commentCreationResult.message_id.ref_block_num,
                "new body", Tag("test"))

        assertTrue("comment update fail on test net", updateResponse is Either.Success)
        val updateResult = (updateResponse as Either.Success).value.extractResult()

        assertEquals("body was not updated", "new body", updateResult.bodymssg)

        Thread.sleep(1_000)

        val updateResponseSecond = client.updateComment(testInMainTestNetAccount.second,
                testInMainTestNetAccount.first, commentCreationResult.message_id.permlink, commentCreationResult.message_id.ref_block_num,
                "new body1", Tag("test"))

        assertTrue("comment update fail on test net", updateResponseSecond is Either.Success)
        val updateResultSecond = (updateResponseSecond as Either.Success).value.extractResult()
        assertEquals("body was not updated", "new body1", updateResultSecond.bodymssg)


    }

    @Test
    fun deleteTest() {

        val postResponse = client.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata)

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val deleteReponse = client.deletePostOrComment(postResult.message_id.permlink, postResult.message_id.ref_block_num)
        assertTrue("comment deleteReponse fail on test net", deleteReponse is Either.Success)

        val postResponseSecond = client.createPost(secondAccount.first, secondAccount.second,
                "тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")), testMetadata)

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
        val postFeed = client.getCommunityPosts("gls", 100, DiscussionTimeSort.SEQUENTIALLY, null)

        val post = (postFeed as Either.Success).value.items[(Math.random() * 99).toInt()]

        val reblogResult = client.reblog(post.contentId.userId.toCyberName(), post.contentId.permlink, post.contentId.refBlockNum)

        assertTrue(reblogResult is Either.Success)

        println(reblogResult)

    }
}