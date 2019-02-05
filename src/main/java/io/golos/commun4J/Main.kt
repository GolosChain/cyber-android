package io.golos.commun4J

import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName

@SuppressWarnings("unused")
fun main(args: Array<String>) {
    val activeUserName = CommunName("destroyer2k")
    val accName = "freya5"
    val pass = "aadgsd23523wtesgdsdt235rsdgtr1"

    val storage = io.golos.commun4J
            .CommunKeyStorage()
            .apply { addAccountKeys(activeUserName, setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ"))) }

    val eos = io.golos.commun4J.Commun4J(keyStorage = storage)


    eos.setUserMetadata(type = "a")

    val result = eos.deleteUserMetadata()
    println(result)


//    val result = eos.createAccount(accName,
//            pass,
//            (AuthUtils::class.java).getResource("/eoscreateacckey.txt").readText(Charset.defaultCharset()))
//    println(result)


//    val createPostResult = eos.createPost("test title", "тестовое тело поста", listOf(io.golos.commun4J.model.Tag("test")))
//    println(createPostResult)
//    assert(createPostResult is io.golos.commun4J.Either.Success)

//    val postPermlink = (createPostResult as io.golos.commun4J.Either.Success).value.processed.action_traces.first().act.data.permlink
//
//    println("updating post")
//    val updateResult = eos.updatePost(activeUserName, postPermlink, "changed title", "changed body", listOf(Tag("test")))
//    assert(updateResult is io.golos.commun4J.Either.Success)
//
//    println("voting")
//    var voteResult = eos.vote(activeUserName, postPermlink, 10_000)
//    print(voteResult)
//    assert(voteResult is io.golos.commun4J.Either.Success)
//
//    println("downvoting")
//    voteResult = eos.vote(activeUserName, postPermlink, -10_000)
//    print(voteResult)
//    assert(voteResult is io.golos.commun4J.Either.Success)
//
//    println("unvoting")
//    val unvoteResult = eos.vote(activeUserName, postPermlink, 0)
//    print(unvoteResult)
//    assert(unvoteResult is io.golos.commun4J.Either.Success)
//
//    println("creating comment")
//    val createCommentResult = eos.createComment("test comment", activeUserName, postPermlink, Tag("test"))
//    assert(createCommentResult is io.golos.commun4J.Either.Success)
//    val commentPermlink = (createCommentResult as io.golos.commun4J.Either.Success).value.processed.action_traces.first().act.data.permlink
//
//    println("voting")
//    val voteForCommentResult = eos.vote(activeUserName, commentPermlink, 10_000)
//    print(voteResult)
//    assert(voteForCommentResult is io.golos.commun4J.Either.Success)
//
//    println("downvoting")
//    voteResult = eos.vote(activeUserName, commentPermlink, -10_000)
//    print(voteResult)
//    assert(voteResult is io.golos.commun4J.Either.Success)
//
//    println("unvoting")
//    val unvoteForCommentResult = eos.vote(activeUserName, commentPermlink, 0)
//    print(unvoteResult)
//    assert(unvoteForCommentResult is io.golos.commun4J.Either.Success)
//
//    println("deleting post")
//
//    eos.deletePostOrComment(commentPermlink)
}






