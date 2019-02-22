import io.golos.commun4J.Commun4J
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.Tag
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.Pair
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

class PostingTest {
    private val privateTestNetClient = Commun4J(privateTestNetConfig)

    @Before
    fun before() {
        privateTestNetClient.keyStorage.addAccountKeys(testingAccountInPrivateTestNet.first,
                setOf(Pair(AuthType.ACTIVE, testingAccountInPrivateTestNet.second)))
    }

    @Test
    fun testPostOnPrivateTestNet() {

        val postResponse = privateTestNetClient.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")))

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val commentCreationResult = privateTestNetClient.createComment("тестовый коммент",
                postResult.message_id.author, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                Tag("test"))

        assertTrue("comment creation fail on test net", commentCreationResult is Either.Success)


        val secondPostResponse = privateTestNetClient.createPost(testingAccountInPrivateTestNet.first, testingAccountInPrivateTestNet.second, "тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")))
        assertTrue("post creation fail on test net", secondPostResponse is Either.Success)

        val secondPostResult = (secondPostResponse as Either.Success).value.extractResult()

        val secondCommentCreationResult = privateTestNetClient.createComment(testingAccountInPrivateTestNet.first, testingAccountInPrivateTestNet.second,
                "тестовый коммент",
                secondPostResult.message_id.author, secondPostResult.message_id.permlink, secondPostResult.message_id.ref_block_num,
                Tag("test"))

        assertTrue("comment creation fail on test net", secondCommentCreationResult is Either.Success)

    }

    @Test
    fun updatePostTest() {

        val postResponse = privateTestNetClient.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")))

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.extractResult()

        val updateResponse = privateTestNetClient.updatePost(postResult.message_id.permlink, postResult.message_id.ref_block_num,
                "new title", "new body", listOf(Tag("test")))

        assertTrue("post update fail on test net", updateResponse is Either.Success)
        val updateResult = (updateResponse as Either.Success).value.extractResult()
        assertEquals("title was not updated", "new title", updateResult.headermssg)
        assertEquals("body was not updated", "new body", updateResult.bodymssg)

        Thread.sleep(1_000)

        val updateResponseSecond = privateTestNetClient.updatePost(testingAccountInPrivateTestNet.second,
                testingAccountInPrivateTestNet.first, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                "new title1", "new body1", listOf(Tag("test")))

        assertTrue("post update fail on test net", updateResponseSecond is Either.Success)
        val updateResultSecond = (updateResponseSecond as Either.Success).value.extractResult()
        assertEquals("title was not updated", "new title1", updateResultSecond.headermssg)
        assertEquals("body was not updated", "new body1", updateResultSecond.bodymssg)

        val deleteReponse = privateTestNetClient.deletePostOrComment(updateResultSecond.message_id.permlink, updateResultSecond.message_id.ref_block_num)
        assertTrue("post deleteReponse fail on test net", deleteReponse is Either.Success)

    }
}