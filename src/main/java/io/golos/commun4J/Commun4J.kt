package io.golos.commun4J

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.http.rpc.ChainApi
import com.squareup.moshi.Moshi
import io.golos.commun4J.model.*
import net.gcardone.junidecode.Junidecode
import java.util.concurrent.Callable

private enum class CommunActions {
    CREATE_MESSAGE, UPDATE_MESSAGE, DELETE_MESSAGE, UP_VOTE, DOWN_VOTE, UN_VOTE, ;

    override fun toString(): String {
        return when (this) {
            CREATE_MESSAGE -> "createmssg"
            UPDATE_MESSAGE -> "updatemssg"
            UP_VOTE -> "upvote"
            DOWN_VOTE -> "downvote"
            UN_VOTE -> "unvote"
            DELETE_MESSAGE -> "deletemssg"
        }
    }

}

private enum class CommuntContract {
    PUBLICATION;

    override fun toString(): String {
        return when (this) {
            PUBLICATION -> "gls.publish"
        }
    }


}

class Commun4J(config: io.golos.commun4J.Commun4JConfig = io.golos.commun4J.Commun4JConfig(),
               chainApiProvider: io.golos.commun4J.ChainApiProvider? = null,
               private val moshi: Moshi = Moshi
                       .Builder()
                       .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                       .build()) {
    private val staleTransactionErrorCode = 3080006

    private val transactionPusher: io.golos.commun4J.TransactionPusher
    private val chainApi: ChainApi

    init {
        if (chainApiProvider == null) {
            chainApi = io.golos.commun4J.GolosEosConfiguratedApi(config, io.golos.commun4J.GolosEosConfiguratedApi.LogLevel.BODY).provide()
            this.transactionPusher = io.golos.commun4J.GolosEosTransactionPusher(chainApi, config, moshi)
        } else {
            this.transactionPusher = io.golos.commun4J.GolosEosTransactionPusher(chainApiProvider.provide(), config, moshi)
            chainApi = chainApiProvider.provide()
        }
    }

    // create post, using active account from CommunKeyStorage

    fun createPost(title: String,
                   body: String,
                   tags: List<io.golos.commun4J.model.Tag>,
                   beneficiaries: List<io.golos.commun4J.model.Beneficiary> = emptyList(),
                   vestPayment: Boolean = true,
                   tokenProp: Long = 0L): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {

        val activeAccountName = io.golos.commun4J.CommunKeyStorage.getActiveAccount()
        val activeAccountKey = io.golos.commun4J.CommunKeyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return createPost(activeAccountName,
                activeAccountKey,
                title,
                body,
                tags,
                beneficiaries,
                vestPayment,
                tokenProp)
    }

    fun createPost(fromAccount: CommunName,
                   userActiveKey: String,
                   title: String,
                   body: String,
                   tags: List<io.golos.commun4J.model.Tag>,
                   beneficiaries: List<io.golos.commun4J.model.Beneficiary> = emptyList(),
                   vestPayment: Boolean = true,
                   tokenProp: Long = 0L): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {

        return createPostOrComment(fromAccount, userActiveKey,
                title, body, "${Junidecode.unidecode(title)}-${System.currentTimeMillis()}",
                "", CommunName(), tags, beneficiaries, vestPayment, tokenProp)
    }

    private fun isStateError(callResult: Either<out Any?, GolosEosError>): Boolean {
        return callResult is Either.Failure && callResult.value.code == staleTransactionErrorCode
    }

    private fun callTilTimeoutExceptionVanishes(callable: Callable<Either<TransactionSuccessful, GolosEosError>>): Either<TransactionSuccessful, GolosEosError> {
        var result: Either<TransactionSuccessful, GolosEosError>
        do {
            result = callable.call()
        } while (isStateError(result))

        return result
    }

    private fun pushTransaction(contractAccount: CommuntContract,
                                actionName: CommunActions,
                                authorization: MyTransactionAuthorizationAbi,
                                data: String,
                                key: String): Either<TransactionSuccessful, GolosEosError> {
        return transactionPusher.pushTransaction(listOf(MyActionAbi(contractAccount.toString(), actionName.toString(), listOf(authorization), data)), EosPrivateKey(key))
    }

    private fun createPostOrComment(fromAccount: CommunName,
                                    userActiveKey: String,
                                    title: String,
                                    body: String,
                                    permlink: String,
                                    parentPermlink: String,
                                    parentAccount: CommunName,
                                    tags: List<io.golos.commun4J.model.Tag>,
                                    beneficiaries: List<io.golos.commun4J.model.Beneficiary> = emptyList(),
                                    vestPayment: Boolean = true,
                                    tokenProp: Long = 0L): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {

        val callable = Callable<io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError>> {
            val createPostRequest = io.golos.commun4J.model.CreatePostRequest(
                    fromAccount.name,
                    permlink,
                    parentAccount.name,
                    parentPermlink,
                    beneficiaries,
                    tokenProp,
                    vestPayment,
                    title,
                    body,
                    tags,
                    "ru",
                    "")

            print("post ${moshi.adapter<io.golos.commun4J.model.CreatePostRequest>(io.golos.commun4J.model.CreatePostRequest::class.java).toJson(createPostRequest)}")

            val result = AbiBinaryGenCommun4J(CompressionType.NONE).squishCreatePostRequest(createPostRequest)
            pushTransaction(CommuntContract.PUBLICATION, CommunActions.CREATE_MESSAGE, MyTransactionAuthorizationAbi(fromAccount.name), result.toHex(), userActiveKey)
        }

        return callTilTimeoutExceptionVanishes(callable)
    }


    fun createComment(body: String,
                      parentAccount: CommunName,
                      parentPermlink: String,
                      category: Tag,
                      beneficiaries: List<io.golos.commun4J.model.Beneficiary> = emptyList(),
                      vestPayment: Boolean = true,
                      tokenProp: Long = 0L): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {
        val activeAccountName = io.golos.commun4J.CommunKeyStorage.getActiveAccount()
        val activeAccountKey = io.golos.commun4J.CommunKeyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return createComment(activeAccountName,
                activeAccountKey,
                body,
                parentAccount,
                parentPermlink,
                category,
                beneficiaries,
                vestPayment,
                tokenProp)
    }

    fun createComment(fromAccount: CommunName,
                      userActiveKey: String,
                      body: String,
                      parentAccount: CommunName,
                      parentPermlink: String,
                      category: Tag,
                      beneficiaries: List<io.golos.commun4J.model.Beneficiary> = listOf(),
                      vestPayment: Boolean = true,
                      tokenProp: Long = 0L): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {


        return createPostOrComment(fromAccount, userActiveKey, "", body,
                "re-$parentPermlink-${System.currentTimeMillis()}", parentPermlink,
                parentAccount, listOf(category), beneficiaries, vestPayment, tokenProp)
    }

    private fun updatePostOrComment(postAuthor: CommunName,
                                    userActiveKey: String,
                                    newPostAuthor: CommunName,
                                    newPermlink: String,
                                    newTitle: String,
                                    newBody: String,
                                    newLanguage: String,
                                    newTags: List<Tag>,
                                    newJsonMetadata: String): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {
        val callable = Callable {
            val updateRequest = UpdatePostRequest(newPostAuthor, newPermlink, newTitle, newBody,
                    newLanguage, newTags, newJsonMetadata)
            print("updateRequest = ${moshi.adapter(UpdatePostRequest::class.java).toJson(updateRequest)}")
            pushTransaction(CommuntContract.PUBLICATION,
                    CommunActions.UPDATE_MESSAGE,
                    MyTransactionAuthorizationAbi(postAuthor.name), AbiBinaryGenCommun4J(CompressionType.NONE).squishUpdatePostRequest(updateRequest).toHex(), userActiveKey)
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    fun updatePost(postAuthor: CommunName,
                   userActiveKey: String,
                   newPostAuthor: CommunName,
                   newPermlink: String,
                   newTitle: String,
                   newBody: String,
                   newTags: List<Tag>): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {
        return updatePostOrComment(postAuthor, userActiveKey, newPostAuthor, newPermlink, newTitle, newBody, "ru", newTags, "")
    }

    fun updateComment(postAuthor: CommunName,
                      userActiveKey: String,
                      newPostAuthor: CommunName,
                      newPermlink: String,
                      newBody: String,
                      newCategory: Tag): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {

        return updatePostOrComment(postAuthor, userActiveKey, newPostAuthor, newPermlink, "", newBody, "ru", listOf(newCategory), "")
    }

    fun updatePost(newPostAuthor: CommunName,
                   newPermlink: String,
                   newTitle: String,
                   newBody: String,
                   newTags: List<Tag>): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {
        val activeAccountName = io.golos.commun4J.CommunKeyStorage.getActiveAccount()
        val activeAccountKey = io.golos.commun4J.CommunKeyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return updatePostOrComment(activeAccountName, activeAccountKey, newPostAuthor, newPermlink, newTitle, newBody, "ru", newTags, "")
    }

    fun updateComment(newPostAuthor: CommunName,
                      newPermlink: String,
                      newBody: String,
                      newCategory: Tag): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {

        val activeAccountName = io.golos.commun4J.CommunKeyStorage.getActiveAccount()
        val activeAccountKey = io.golos.commun4J.CommunKeyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return updatePostOrComment(activeAccountName, activeAccountKey, newPostAuthor, newPermlink, "", newBody, "ru", listOf(newCategory), "")
    }

    fun deletePostOrComment(postAuthor: CommunName,
                            userActiveKey: String,
                            permlink: String): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {
        val callable = Callable {
            pushTransaction(CommuntContract.PUBLICATION,
                    CommunActions.DELETE_MESSAGE,
                    MyTransactionAuthorizationAbi(postAuthor),
                    AbiBinaryGenCommun4J(CompressionType.NONE).squishDeleteMessageRequest(DeleteMessageRequest(postAuthor, permlink)).toHex(),
                    userActiveKey)
        }

        return callTilTimeoutExceptionVanishes(callable)
    }

    fun deletePostOrComment(permlink: String): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {
        val activeAccountName = io.golos.commun4J.CommunKeyStorage.getActiveAccount()
        val activeAccountKey = io.golos.commun4J.CommunKeyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return deletePostOrComment(activeAccountName, activeAccountKey, permlink)
    }

    //vote strength -10000 _+10000
    fun vote(postAuthor: CommunName,
             postPermlink: String,
             voteStrength: Short): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {
        val activeAccountName = io.golos.commun4J.CommunKeyStorage.getActiveAccount()
        val activeAccountKey = io.golos.commun4J.CommunKeyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return vote(activeAccountName, activeAccountKey, postAuthor, postPermlink, voteStrength)
    }

    //vote strength -10000 _+10000
    fun vote(fromAccount: CommunName,
             userActiveKey: String,
             postAuthor: CommunName,
             postPermlink: String,
             voteStrength: Short): io.golos.commun4J.Either<TransactionSuccessful, io.golos.commun4J.model.GolosEosError> {
        val callable = Callable {
            val squisher = AbiBinaryGenCommun4J(CompressionType.NONE)

            val operationHex = if (voteStrength == 0.toShort()) squisher.squishUnVoteRequest(io.golos.commun4J.model.UnVoteRequest(fromAccount, postAuthor, postPermlink)).toHex()
            else squisher.squishVoteRequest(io.golos.commun4J.model.VoteRequest(fromAccount, postAuthor, postPermlink,
                    Math.abs(voteStrength.toInt()).toShort())).toHex()

            pushTransaction(CommuntContract.PUBLICATION,
                    if (voteStrength == 0.toShort()) CommunActions.UN_VOTE else if (voteStrength > 0) CommunActions.UP_VOTE else CommunActions.DOWN_VOTE,
                    MyTransactionAuthorizationAbi(fromAccount.name),
                    operationHex,
                    userActiveKey)

        }
        return callTilTimeoutExceptionVanishes(callable)

    }

}