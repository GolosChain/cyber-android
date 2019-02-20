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

        val postResult = (postResponse as Either.Success).value.processed.action_traces.first().act.data

        val commentCreationResult = privateTestNetClient.createComment("тестовый коммент",
                postResult.message_id.author, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                Tag("test"))

        assertTrue("comment creation fail on test net", commentCreationResult is Either.Success)

    }

    @Test
    fun testUpdatePostOnPrivateTestNet() {
        val postResponse = privateTestNetClient.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")))

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.processed.action_traces.first().act.data

        val updateResult = privateTestNetClient.updatePost(postResult.message_id.permlink, postResult.message_id.ref_block_num,
                "new_title", "new_body", listOf(Tag("test")))

        assertTrue("post updating error on test net", updateResult is Either.Success)
        val updatedPost = (updateResult as Either.Success).value.processed.action_traces.first().act.data

        assertEquals("title didn't changed", "new_title", updatedPost.headermssg)
        assertEquals("body didn't changed", "new_body", updatedPost.bodymssg)

        val deleteResult = privateTestNetClient.deletePostOrComment(updatedPost.message_id.permlink, updatedPost.message_id.ref_block_num)
        assertTrue("post deletion fail on test net", deleteResult is Either.Success)
    }
}