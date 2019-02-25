import io.golos.commun4J.Commun4J
import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.model.DiscussionTimeSort
import io.golos.commun4J.utils.Either
import org.junit.Assert.assertTrue
import org.junit.Test

class ServicesFetchTest {
    private val commun4J = Commun4J(Commun4JConfig(servicesUrl = "ws://116.203.98.241:8080"))

    @Test
    fun fetchPostsTest() {
        val postsResponse = commun4J.getCommunityPosts(
                "gls",
                20,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(postsResponse is Either.Success)

        val posts = (postsResponse as Either.Success).value.items

        assertTrue("posts feed is empty", posts.isNotEmpty())

        val post = posts.last()

        val postResponse = commun4J.getPost(
                post.contentId.userId.toCommunName(),
                post.contentId.permlink,
                post.contentId.refBlockNum)

        assertTrue(postResponse is Either.Success)

        val comments = commun4J.getCommentsOfPost(
                post.author.userId.name.toCommunName(), post.contentId.permlink, post.contentId.refBlockNum,
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(comments is Either.Success)

        val commentsOfUser = commun4J.getCommentsOfUser(
                post.author.userId.name.toCommunName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(commentsOfUser is Either.Success)

        val subscriptionsOfUser =  commun4J.getUserSubsriptions(
                post.author.userId.name.toCommunName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(subscriptionsOfUser is Either.Success)

        val postsOfUser =  commun4J.getUserPosts(
                post.author.userId.name.toCommunName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(postsOfUser is Either.Success)

    }

    @Test
    fun userMetadataFetchTest() {
        val response = commun4J.getUserMetadata("destroyer2k".toCommunName())
        assertTrue(response is Either.Success)
    }
}