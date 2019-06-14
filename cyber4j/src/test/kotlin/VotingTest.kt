import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.CreateDiscussionResult
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.model.DiscussionCreateMetadata
import io.golos.cyber4j.model.Tag
import io.golos.cyber4j.utils.Either
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VotingTest {

    private lateinit var client: Cyber4J
    private lateinit var postCreateResult: CreateDiscussionResult
    private lateinit var secndTestAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        client = getClient()
        postCreateResult = (client.createPost("sdgsdg", "gdssdg",
                listOf(Tag("test")), DiscussionCreateMetadata(emptyList(), listOf()), 0)
                as Either.Success).value.processed.action_traces.first().act.data

        secndTestAccount = account(client.config.toConfigType())
    }

    @Test
    fun voteTest() {
        val upvoteResult = client.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, 10_000)

        assertTrue("upvote fail", upvoteResult is Either.Success)

        val upvoteResultSecond = client.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, 5_000)

        assertTrue("upvote fail", upvoteResultSecond is Either.Success)

        val downVoteResult = client.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, -10_000)
        assertTrue("downvote fail", downVoteResult is Either.Success)

        val downVoteResultSecond = client.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, -10_000)
        assertTrue("downvote fail", downVoteResultSecond is Either.Success)

        val unvoteResult = client.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, 0)

        assertTrue("unvote fail", unvoteResult is Either.Success)

        val unvoteResultSecond = client.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, 0)

        assertTrue("unvote fail", unvoteResultSecond is Either.Success)
    }
}