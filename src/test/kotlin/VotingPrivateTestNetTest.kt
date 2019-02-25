import io.golos.commun4J.Commun4J
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import io.golos.commun4J.model.CreateDiscussionResult
import io.golos.commun4J.model.Tag
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.Pair
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VotingPrivateTestNetTest {

    private val commun4J = Commun4J(privateTestNetConfig)
    private lateinit var postCreateResult: CreateDiscussionResult
    private lateinit var secndTestAccount: kotlin.Pair<CommunName, String>

    @Before
    fun before() {
        commun4J.keyStorage.addAccountKeys(testingAccountInPrivateTestNet.first,
                setOf(Pair(AuthType.ACTIVE, testingAccountInPrivateTestNet.second)))
        postCreateResult = (commun4J.createPost("sdgsdg", "gdssdg", listOf(Tag("test")))
                as Either.Success).value.processed.action_traces.first().act.data

        secndTestAccount = testingAccountInPrivateTestNetSecond
    }

    @Test
    fun voteTest() {
        val upvoteResult = commun4J.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, 10_000)

        assertTrue("upvote fail", upvoteResult is Either.Success)

        val upvoteResultSecond = commun4J.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, postCreateResult.message_id.ref_block_num, 5_000)

        assertTrue("upvote fail", upvoteResultSecond is Either.Success)

        val downVoteResult = commun4J.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, -10_000)
        assertTrue("downvote fail", downVoteResult is Either.Success)

        val downVoteResultSecond = commun4J.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, -10_000)
        assertTrue("downvote fail", downVoteResultSecond is Either.Success)

        val unvoteResult = commun4J.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, 0)

        assertTrue("unvote fail", unvoteResult is Either.Success)

        val unvoteResultSecond = commun4J.vote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink,
                postCreateResult.message_id.ref_block_num, 0)

        assertTrue("unvote fail", unvoteResultSecond is Either.Success)
    }
}