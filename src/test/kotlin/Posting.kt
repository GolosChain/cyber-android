import io.golos.commun4J.Commun4J
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.Tag
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.Pair
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class PostingTest {

    @Test
    fun testPostOnTestNet() {
        val acc = AccountCreationTest.createNewAccount(privateTestNetConfig)
        val commun4J = Commun4J(privateTestNetConfig)
        commun4J.keyStorage.addAccountKeys(testingAccountInPrivateTestNet.first, setOf(Pair(AuthType.ACTIVE, testingAccountInPrivateTestNet.second)))

        val postResponse = commun4J.createPost("тестовый заголовок-${UUID.randomUUID()}",
                "тестовое тело поста", listOf(Tag("test")))

        assertTrue("post creation fail on test net", postResponse is Either.Success)

        val postResult = (postResponse as Either.Success).value.processed.action_traces.first().act.data

        val commentCreationResult = commun4J.createComment("тестовый коммент",
                postResult.message_id.author, postResult.message_id.permlink, postResult.message_id.ref_block_num,
                Tag("test"))

        assertTrue("comment creation fail on test net", commentCreationResult is Either.Success)

    }
}