import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.DiscussionTimeSort
import io.golos.cyber4j.utils.Either
import org.junit.Assert.assertTrue
import org.junit.Test

class ServicesFetchTest {
    private val client = Cyber4J(Cyber4JConfig(servicesUrl = "ws://116.203.98.241:8080"))

    @Test
    fun fetchPostsTest() {
        val postsResponse = client.getCommunityPosts(
                "gls",
                20,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(postsResponse is Either.Success)

        val posts = (postsResponse as Either.Success).value.items

        assertTrue("posts feed is empty", posts.isNotEmpty())

        val post = posts.last()

        val postResponse = client.getPost(
                post.contentId.userId.toCyberName(),
                post.contentId.permlink,
                post.contentId.refBlockNum)

        assertTrue(postResponse is Either.Success)

        val comments = client.getCommentsOfPost(
                post.author.userId.name.toCyberName(), post.contentId.permlink, post.contentId.refBlockNum,
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(comments is Either.Success)

        val commentsOfUser = client.getCommentsOfUser(
                post.author.userId.name.toCyberName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(commentsOfUser is Either.Success)

        val subscriptionsOfUser =  client.getUserSubsriptions(
                post.author.userId.name.toCyberName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(subscriptionsOfUser is Either.Success)

        val postsOfUser =  client.getUserPosts(
                post.author.userId.name.toCyberName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(postsOfUser is Either.Success)

    }

    @Test
    fun userMetadataFetchTest() {
        val response = client.getUserMetadata("destroyer2k".toCyberName())
        assertTrue(response is Either.Success)
    }
}