import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.ContentParsingType
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
                ContentParsingType.MOBILE,
                100,
                DiscussionTimeSort.INVERTED,
                null
        )

        assertTrue(postsResponse is Either.Success)

        val posts = (postsResponse as Either.Success).value.items

        assertTrue("posts feed is empty", posts.isNotEmpty())

        val post = posts[10]

        val postResponse = client.getPost(
                post.contentId.userId.toCyberName(),
                post.contentId.permlink,
                post.contentId.refBlockNum,
                ContentParsingType.MOBILE
        )

        assertTrue(postResponse is Either.Success)

        val comments = client.getCommentsOfPost(
                post.author!!.userId.name.toCyberName(), post.contentId.permlink, post.contentId.refBlockNum,
                ContentParsingType.RAW,
                100,
                DiscussionTimeSort.INVERTED,
                null
        )

        assertTrue(comments is Either.Success)

        val commentsOfUser = client.getCommentsOfUser(
                post.author!!.userId.name.toCyberName(),
                ContentParsingType.MOBILE,
                100,
                DiscussionTimeSort.INVERTED,
                null
        )

        assertTrue(commentsOfUser is Either.Success)

        val subscriptionsOfUser = client.getUserSubscriptions(
                post.author!!.userId.name.toCyberName(),
                ContentParsingType.MOBILE,
                100,
                DiscussionTimeSort.INVERTED,
                null
        )

        assertTrue(subscriptionsOfUser is Either.Success)

        val postsOfUser = client.getUserPosts(
                post.author!!.userId.name.toCyberName(),
                ContentParsingType.MOBILE,
                100,
                DiscussionTimeSort.INVERTED,
                null
        )

        assertTrue(postsOfUser is Either.Success)

    }

    @Test
    fun getCommentTest() {
        val postsResponse = client.getCommunityPosts(
                "gls",
                ContentParsingType.MOBILE,
                20,
                DiscussionTimeSort.INVERTED,
                null
        )

        assertTrue(postsResponse is Either.Success)

        val posts = (postsResponse as Either.Success).value.items

        val postWithComments = posts.find { it.stats?.commentsCount ?: 0 > 0 }!!

        val comments = client.getCommentsOfPost(
                postWithComments.contentId.userId.toCyberName(),
                postWithComments.contentId.permlink,
                postWithComments.contentId.refBlockNum,
                ContentParsingType.MOBILE,
                1, DiscussionTimeSort.SEQUENTIALLY, null
        )

        assertTrue(comments is Either.Success)

        val comment = (comments as Either.Success).value.items.first()

        val fetchedComment = client.getComment(
                comment.contentId.userId.toCyberName(),
                comment.contentId.permlink, comment.contentId.refBlockNum,
                ContentParsingType.MOBILE
        )

        assertTrue(fetchedComment is Either.Success)

    }

    @Test
    fun userMetadataFetchTest() {
        val response = client.getUserMetadata("destroyer2k@golos".toCyberName())
        assertTrue(response is Either.Success)
    }

    @Test
    fun userProfileTest() {
        val response = client.getUserAccount("destroyer2k@golos".toCyberName())
        assertTrue(response is Either.Success)
    }

    @Test
    fun getEmbedTest() {
        val response =
                client.getEmbedIframely("https://music.yandex.ru/album/3100408/track/3869231")
        assertTrue(response is Either.Success)

    }
}