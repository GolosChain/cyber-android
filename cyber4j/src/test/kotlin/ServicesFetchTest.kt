import io.golos.cyber4j.model.DiscussionCreateMetadata
import io.golos.cyber4j.model.Tag
import io.golos.cyber4j.services.model.ContentParsingType
import io.golos.cyber4j.services.model.DiscussionTimeSort
import io.golos.sharedmodel.Either
import io.golos.sharedmodel.CyberName
import org.junit.Assert.assertTrue
import org.junit.Test

class ServicesFetchTest {
    private val client = getClient()

    @Test
    fun fetchPostsTest() {
        val postsResponse = client.getCommunityPosts(
                "gls",
                ContentParsingType.MOBILE,
                11,
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
                ContentParsingType.MOBILE
        )

        assertTrue(postResponse is Either.Success)

        val comments = client.getCommentsOfPost(
                post.author!!.userId.name.toCyberName(), post.contentId.permlink,
                ContentParsingType.RAW,
                100,
                DiscussionTimeSort.INVERTED,
                null
        )

        assertTrue(comments is Either.Success)

        val commentsOfUser = client.getCommentsOfUser(
                post.author!!.userId.name.toCyberName(),
                ContentParsingType.MOBILE,
                10,
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
                ContentParsingType.MOBILE,
                1, DiscussionTimeSort.SEQUENTIALLY, null
        )

        assertTrue(comments is Either.Success)

        val comment = (comments as Either.Success).value.items.first()

        val fetchedComment = client.getComment(
                comment.contentId.userId.toCyberName(),
                comment.contentId.permlink,
                ContentParsingType.MOBILE
        )

        assertTrue(fetchedComment is Either.Success)

        val comments1 = client.getUserReplies(postWithComments.contentId.userId.toCyberName(),
                ContentParsingType.WEB, 10, DiscussionTimeSort.INVERTED, null)
        assertTrue(comments1 is Either.Success)

    }

    @Test
    fun userMetadataFetchTest() {
        val response = client.getUserMetadata("uswjtsevydjx".toCyberName())
        assertTrue(response is Either.Success)
    }

    @Test
    fun userProfileTest() {
        val response = client.getUserAccount(
                "uswjtsevydjx".toCyberName())
        assertTrue(response is Either.Success)
    }

    @Test
    fun getEmbedTest() {
        val response =
                client.getEmbedIframely("https://music.yandex.ru/album/3100408/track/3869231")
        assertTrue(response is Either.Success)

    }


    @Test
    fun testSubscriptionsAndSubscribers() {
        val subscriptionsToUsers = client.getSubscriptionsToUsers(CyberName("destroyer2k"), 10, null)
        assertTrue((subscriptionsToUsers as Either.Success).value.items.isNotEmpty())
        val subscriptionsToCommunitites = client.getSubscriptionsToCommunities(CyberName("destroyer2k@golos"), 10, null)
        assertTrue((subscriptionsToCommunitites as Either.Success).value.items.isNotEmpty())

        val subscribedUsers = client.getUsersSubscribedToUser(CyberName("destroyer2k@golos"), 10, null)
        assertTrue((subscribedUsers as Either.Success).value.items.isNotEmpty())
        val subscribedCommunitites = client.getCommunitiesSubscribedToUser(CyberName("destroyer2k@golos"), 10, null)
        assertTrue((subscribedCommunitites as Either.Success).value.items.isNotEmpty())
    }

    @Test
    fun waitForABlockTest() {
        val result = client.createPost("",
                "post",
                emptyList<Tag>(),
                DiscussionCreateMetadata(emptyList(), emptyList()),
                null)
        val blockNum = (result as Either.Success).value.processed.block_num!!
        val waitResult = client.waitForABlock(blockNum.toLong())
        assertTrue(waitResult is Either.Success)
    }

    @Test
    fun waitForTransactionTest() {

        val result = client.createPost("",
                "post",
                emptyList<Tag>(),
                DiscussionCreateMetadata(emptyList(), emptyList()),
                null)
        val blockId = (result as Either.Success).value.processed.id
        val waitResult = client.waitForTransaction(blockId)
        assertTrue(waitResult is Either.Success)
    }
}