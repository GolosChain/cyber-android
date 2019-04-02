import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.*
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VotingTest {

    private val client = Cyber4J(mainTestNetConfig)
    private lateinit var postCreateResult: CreateDiscussionResult
    private lateinit var secndTestAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        client.keyStorage.addAccountKeys(testInMainTestNetAccount.first,
                setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)))
        postCreateResult = (client.createPost("sdgsdg", "gdssdg", listOf(Tag("test")), DiscussionCreateMetadata(emptyList(), listOf()))
                as Either.Success).value.processed.action_traces.first().act.data

        secndTestAccount = testInMainTestNetAccountSecond
    }

    @Test
    fun voteTest() {
        val upvoteResult = client.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, 10_000)

        assertTrue("upvote fail", upvoteResult is Either.Success)

        val upvoteResultSecond = client.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, postCreateResult.message_id.ref_block_num, 5_000)

        assertTrue("upvote fail", upvoteResultSecond is Either.Success)

        val downVoteResult = client.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, -10_000)
        assertTrue("downvote fail", downVoteResult is Either.Success)

        val downVoteResultSecond = client.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, -10_000)
        assertTrue("downvote fail", downVoteResultSecond is Either.Success)

        val unvoteResult = client.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, 0)

        assertTrue("unvote fail", unvoteResult is Either.Success)

        val unvoteResultSecond = client.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, 0)

        assertTrue("unvote fail", unvoteResultSecond is Either.Success)
    }
}