import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.squareup.moshi.Types
import io.golos.commun4J.Commun4J
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunDiscussion
import io.golos.commun4J.model.CreateDiscussionResult
import io.golos.commun4J.model.Tag
import io.golos.commun4J.utils.BigIntegerAdapter
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.Pair
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*

class VotingTest {

    private val postsText = (this::class.java).getResource("/posts.json").readText(Charset.defaultCharset())
    private val post = Moshi.Builder()
            .add(Date::
            class.java, Rfc3339DateJsonAdapter())
            .add(BigInteger::
            class.java, BigIntegerAdapter())
            .build()
            .adapter<List<CommunDiscussion>>(Types.newParameterizedType(List::class.java, CommunDiscussion::class.java))
            .fromJson(postsText)!!.first()

    private val commun4J = Commun4J(mainTestNetConfig)
    private lateinit var postCreateResult: CreateDiscussionResult

    @Before
    fun before() {
        commun4J.keyStorage.addAccountKeys(testInMainTestNetAccount.first,
                setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)))
        postCreateResult = (commun4J.createPost("sdgsdg", "gdssdg", listOf(Tag("test")))
                as Either.Success).value.processed.action_traces.first().act.data
    }

    @Test
    fun voteTest() {
        val upvoteResult = commun4J.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, postCreateResult.message_id.ref_block_num, 10_000)

        assertTrue("upvote fail", upvoteResult is Either.Success)

        val downVoteResult = commun4J.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, postCreateResult.message_id.ref_block_num, -10_000)
        assertTrue("downvote fail", downVoteResult is Either.Success)

        val unvoteResult = commun4J.vote(postCreateResult.message_id.author,
                postCreateResult.message_id.permlink, postCreateResult.message_id.ref_block_num, 0)

        assertTrue("unvote fail", downVoteResult is Either.Success)
    }
}