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
                post.author!!.userId.name.toCyberName(), post.contentId.permlink, post.contentId.refBlockNum,
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(comments is Either.Success)

        val commentsOfUser = client.getCommentsOfUser(
                post.author!!.userId.name.toCyberName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(commentsOfUser is Either.Success)

        val subscriptionsOfUser = client.getUserSubscriptions(
                post.author!!.userId.name.toCyberName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(subscriptionsOfUser is Either.Success)

        val postsOfUser = client.getUserPosts(
                post.author!!.userId.name.toCyberName(),
                10,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(postsOfUser is Either.Success)

    }

    @Test
    fun getCommentTest() {
        val postsResponse = client.getCommunityPosts(
                "gls",
                20,
                DiscussionTimeSort.INVERTED,
                null)

        assertTrue(postsResponse is Either.Success)

        val posts = (postsResponse as Either.Success).value.items

        val postWithComments = posts.find { it.stats?.commentsCount ?: 0 > 0 }!!

        val comments = client.getCommentsOfPost(postWithComments.contentId.userId.toCyberName(),
                postWithComments.contentId.permlink,
                postWithComments.contentId.refBlockNum, 1, DiscussionTimeSort.SEQUENTIALLY, null)

        assertTrue(comments is Either.Success)

        val comment = (comments as Either.Success).value.items.first()

        val fetchedComment = client.getComment(comment.contentId.userId.toCyberName(),
                comment.contentId.permlink, comment.contentId.refBlockNum)

        assertTrue(fetchedComment is Either.Success)

    }

    @Test
    fun userMetadataFetchTest() {
        val response = client.getUserMetadata("destroyer2k".toCyberName())
        assertTrue(response is Either.Success)
    }

    @Test
    fun userProfileTest() {
        val response = client.getUserAccount("destroyer2k".toCyberName())
        assertTrue(response is Either.Success)
    }

    @Test
    fun getEmbedTest() {
        val response = client.getEmbedIframely("https://www.reddit.com/r/RoastMe/comments/b6n9k7/my_boyfriend_hasnt_had_sex_with_me_in_7_months/")
        assertTrue(response is Either.Success)

        val oembedResponse = client.getEmbedOembed("https://www.rbc.ru/business/01/04/2019/5ca1e5719a7947645980c6c5?from=from_main")
        assertTrue(oembedResponse is Either.Success)
    }
}