package io.golos.commun4J

import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.Pair

@SuppressWarnings("unused")
fun main(args: Array<String>) {
    val activeUserName = CommunName("destroyer2k")
    val accName = "freya11"
    val pass = "aadgsd23523wtesgdsdt235rsdgtr1"

    val storage = io.golos.commun4J
            .CommunKeyStorage()
            .apply { addAccountKeys(activeUserName, setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ"))) }

    val eos = io.golos.commun4J.Commun4J(keyStorage = storage, config = Commun4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/",
            isPrivateTestNet = true))

    var pinResult = eos.pin(CommunName(accName))
    pinResult as Either.Success
    println(pinResult)
    pinResult = eos.unPin(CommunName(accName))
    println(pinResult)


//    val discussionsString = (AuthUtils::class.java).getResource("/test.json").readText(Charset.defaultCharset())
//
//    val type = Types.newParameterizedType(List::class.java, CommunDiscussion::class.java)
//
//    val discussionsList = Moshi.Builder()
//            .add(Date::class.java, Rfc3339DateJsonAdapter())
//            .add(BigInteger::class.java, BigIntegerAdapter())
//            .build()
//            .adapter<List<CommunDiscussion>>(type)
//            .fromJson(discussionsString)!!
//
//
//   val discussion = eos.getDiscussions()
////
//    println(discussion)


//
//    val discussions = eos.getDiscussions()
//
//    (discussions as Either.Success).value.first().toString()

    //  eos.getDiscussions()


    // eos.setUserMetadata(app = "appName", type = "typeName", email = "email", phone = "phone")


//    val result = eos.createAccount(accName,
//            pass,
//            (AuthUtils::class.java).getResource("/eoscreateacckey.txt").readText(Charset.defaultCharset()))
//    println(result)
//
//
//    val createPostResult = eos.createPost("test title", "тестовое тело поста", listOf(io.golos.commun4J.model.Tag("test")))
//    println(createPostResult)
//
//    assert(createPostResult is io.golos.commun4J.utils.Either.Success)
//
//    val postId = (createPostResult as io.golos.commun4J.utils.Either.Success).value.processed.action_traces.first().act.data.message_id
//
//    println("updating post")
//    val updateResult = eos.updatePost(postId.permlink, postId.ref_block_num, "changed title", "changed body", listOf(Tag("test")))
//    assert(updateResult is io.golos.commun4J.utils.Either.Success)
//    println(updateResult)
//
//    println("voting")
//    var voteResult = eos.vote(postId.author, postId.permlink, postId.ref_block_num, 10_000) as io.golos.commun4J.utils.Either.Success
//    println(voteResult)
//
//
//    println("downvoting")
//    voteResult = eos.vote(postId.author, postId.permlink, postId.ref_block_num, -10_000) as Either.Success<TransactionSuccessful<VoteResult>, GolosEosError>
//    println(voteResult)
//
//
//    println("unvoting")
//    voteResult = eos.vote(postId.author, postId.permlink, postId.ref_block_num, 0) as Either.Success<TransactionSuccessful<VoteResult>, GolosEosError>
//    println(voteResult)
//
//    println("creating comment")
//    val createCommentResult = eos.createComment("test comment", postId.author,
//            postId.permlink, postId.ref_block_num, Tag("test"), emptyList(), true, 0) as Either.Success
//    println(createCommentResult)
//    println("deleting")
//    val commentId = createCommentResult.value.processed.action_traces.first().act.data.message_id
//
//    val deleteResult = eos.deletePostOrComment(commentId.permlink, commentId.ref_block_num) as Either.Success
//    println(deleteResult)
}






