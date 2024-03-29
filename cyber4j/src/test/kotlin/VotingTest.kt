import io.golos.cyber4j.abi.implementation.publish.CreatemssgPublishStruct
import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.DiscussionCreateMetadata
import io.golos.cyber4j.model.Tag
import io.golos.cyber4j.sharedmodel.CyberName
import io.golos.cyber4j.sharedmodel.Either
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VotingTest {

    private lateinit var client: Cyber4J
    private lateinit var postCreateResult: CreatemssgPublishStruct
    private lateinit var secndTestAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        client = getClient()
        postCreateResult = (client.createPost("sdgsdg", "gdssdg",
                listOf(Tag("test")), DiscussionCreateMetadata(emptyList()), 0)
                as Either.Success).value.resolvedResponse!!

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

        val unvoteResult = client.unVote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink)

        assertTrue("unvote fail", unvoteResult is Either.Success)

        val unvoteResultSecond = client.unVote(secndTestAccount.first,
                secndTestAccount.second,
                postCreateResult.message_id.author,
                postCreateResult.message_id.permlink)

        assertTrue("unvote fail", unvoteResultSecond is Either.Success)
    }
}