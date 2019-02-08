package io.golos.commun4J

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.AbiBinaryGenTransactionWriter
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.AccountKeyAbi
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.AccountRequiredAuthAbi
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.NewAccountArgs
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.NewAccountBody
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.hex.DefaultHexWriter
import com.memtrip.eos.http.rpc.ChainApi
import com.squareup.moshi.Moshi
import io.golos.commun4J.model.*
import io.golos.commun4J.services.CommunServicesApiProvider
import io.golos.commun4J.utils.AuthUtils
import io.golos.commun4J.utils.Either
import net.gcardone.junidecode.Junidecode
import java.util.concurrent.Callable

private enum class CommunActions {
    CREATE_MESSAGE, UPDATE_MESSAGE, DELETE_MESSAGE, UP_VOTE,
    DOWN_VOTE, UN_VOTE, NEW_ACCOUNT, OPEN_VESTING,
    UPDATE_META, DELETE_METADATA;

    override fun toString(): String {
        return when (this) {
            CREATE_MESSAGE -> "createmssg"
            UPDATE_MESSAGE -> "updatemssg"
            UP_VOTE -> "upvote"
            DOWN_VOTE -> "downvote"
            UN_VOTE -> "unvote"
            DELETE_MESSAGE -> "deletemssg"
            NEW_ACCOUNT -> "newaccount"
            OPEN_VESTING -> "open"
            UPDATE_META -> "updatemeta"
            DELETE_METADATA -> "deletemeta"
        }
    }

}

private enum class CommuntContract {
    PUBLICATION, EOSIO, VESTING, SOCIAL;

    override fun toString(): String {
        return when (this) {
            PUBLICATION -> "gls.publish"
            EOSIO -> "eosio"
            VESTING -> "gls.vesting"
            SOCIAL -> "gls.social"
        }
    }


}

class Commun4J @JvmOverloads constructor(config: io.golos.commun4J.Commun4JConfig = io.golos.commun4J.Commun4JConfig(),
                                         chainApiProvider: io.golos.commun4J.ChainApiProvider? = null,
                                         private val historyApiProvider: HistoryApiProvider = CommunServicesApiProvider(config),
                                         val keyStorage: CommunKeyStorage = CommunKeyStorage()) {
    private val staleTransactionErrorCode = 3080006

    private val transactionPusher: io.golos.commun4J.TransactionPusher
    private val chainApi: ChainApi
    private val moshi: Moshi = Moshi
            .Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .add(CommunNameAdapter())
            .build()

    init {
        if (chainApiProvider == null) {
            chainApi = io.golos.commun4J.GolosEosConfiguratedApi(config).provide()
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
                   tokenProp: Long = 0L): Either<TransactionSuccessful<CreatePostResult>, GolosEosError> {

        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
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
                   tokenProp: Long = 0L): Either<TransactionSuccessful<CreatePostResult>, GolosEosError> {

        return createPostOrComment(fromAccount, userActiveKey,
                title, body, "${Junidecode.unidecode(title)}-${System.currentTimeMillis()}",
                "", CommunName(), tags, beneficiaries, vestPayment, tokenProp)
    }

    private fun isStateError(callResult: Either<out Any?, GolosEosError>): Boolean {
        return callResult is Either.Failure && callResult.value.code == staleTransactionErrorCode
    }

    private fun <T> callTilTimeoutExceptionVanishes(callable: Callable<Either<TransactionSuccessful<T>,
            GolosEosError>>): Either<TransactionSuccessful<T>, GolosEosError> {
        var result: Either<TransactionSuccessful<T>, GolosEosError>
        do {
            result = callable.call()
        } while (isStateError(result))

        return result
    }

    private inline fun <reified T> pushTransaction(contractAccount: CommuntContract,
                                                   actionName: CommunActions,
                                                   authorization: MyTransactionAuthorizationAbi,
                                                   data: String,
                                                   key: String): Either<TransactionSuccessful<T>, GolosEosError> {

        return transactionPusher.pushTransaction(listOf(MyActionAbi(contractAccount.toString(),
                actionName.toString(), listOf(authorization), data)),
                EosPrivateKey(key),
                T::class.java)
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
                                    tokenProp: Long = 0L): Either<TransactionSuccessful<CreatePostResult>, GolosEosError> {

        val callable = Callable<Either<TransactionSuccessful<CreatePostResult>, GolosEosError>> {
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

            val result = createBinaryConverter().squishCreatePostRequest(createPostRequest)
            pushTransaction<CreatePostResult>(CommuntContract.PUBLICATION, CommunActions.CREATE_MESSAGE,
                    MyTransactionAuthorizationAbi(fromAccount.name), result.toHex(),
                    userActiveKey)
        }

        return callTilTimeoutExceptionVanishes(callable)
    }


    fun createComment(body: String,
                      parentAccount: CommunName,
                      parentPermlink: String,
                      category: Tag,
                      beneficiaries: List<io.golos.commun4J.model.Beneficiary> = emptyList(),
                      vestPayment: Boolean = true,
                      tokenProp: Long = 0L): Either<TransactionSuccessful<CreatePostResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
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

    fun setUserMetadata(
            type: String? = null,
            app: String? = null,
            email: String? = null,
            phone: String? = null,
            facebook: String? = null,
            instagram: String? = null,
            telegram: String? = null,
            vk: String? = null,
            website: String? = null,
            first_name: String? = null,
            last_name: String? = null,
            name: String? = null,
            birthDate: String? = null,
            gender: String? = null,
            location: String? = null,
            city: String? = null,
            about: String? = null,
            occupation: String? = null,
            iCan: String? = null,
            lookingFor: String? = null,
            businessCategory: String? = null,
            backgroundImage: String? = null,
            coverImage: String? = null,
            profileImage: String? = null,
            userImage: String? = null,
            icoAddress: String? = null,
            targetDate: String? = null,
            targetPlan: String? = null,
            targetPointA: String? = null,
            targetPointB: String? = null): Either<TransactionSuccessful<ProfileMetadatUpdateResult>, GolosEosError> {

        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return setUserMetadata(activeAccountName, activeAccountKey, type, app, email, phone,
                facebook, instagram, telegram, vk, website, first_name, last_name, name, birthDate, gender,
                location, city, about, occupation, iCan, lookingFor, businessCategory, backgroundImage,
                coverImage, profileImage, userImage, icoAddress, targetDate, targetPlan, targetPointA,
                targetPointB)
    }

    fun setUserMetadata(
            fromAccount: CommunName,
            userActiveKey: String,
            type: String? = null,
            app: String? = null,
            email: String? = null,
            phone: String? = null,
            facebook: String? = null,
            instagram: String? = null,
            telegram: String? = null,
            vk: String? = null,
            website: String? = null,
            first_name: String? = null,
            last_name: String? = null,
            name: String? = null,
            birthDate: String? = null,
            gender: String? = null,
            location: String? = null,
            city: String? = null,
            about: String? = null,
            occupation: String? = null,
            iCan: String? = null,
            lookingFor: String? = null,
            businessCategory: String? = null,
            backgroundImage: String? = null,
            coverImage: String? = null,
            profileImage: String? = null,
            userImage: String? = null,
            icoAddress: String? = null,
            targetDate: String? = null,
            targetPlan: String? = null,
            targetPointA: String? = null,
            targetPointB: String? = null): Either<TransactionSuccessful<ProfileMetadatUpdateResult>, GolosEosError> {

        val callable = Callable {
            val request = ProfileMetadataUpdateRequest(fromAccount.name,
                    ProfileMetadata(type, app, email, phone, facebook, instagram,
                            telegram, vk, website, first_name, last_name, name, birthDate, gender, location,
                            city, about, occupation, iCan, lookingFor, businessCategory, backgroundImage, coverImage,
                            profileImage, userImage, icoAddress, targetDate, targetPlan, targetPointA, targetPointB))

            println("request = $request")

            val hex = AbiBinaryGenCommun4J(CompressionType.NONE).squishProfileMetadataUpdateRequest(request).toHex()

            pushTransaction<ProfileMetadatUpdateResult>(CommuntContract.SOCIAL, CommunActions.UPDATE_META, MyTransactionAuthorizationAbi(fromAccount),
                    hex, userActiveKey)
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    fun deleteUserMetadata(fromAccount: CommunName,
                           userActiveKey: String): Either<TransactionSuccessful<ProfileMetadataDeleteResult>, GolosEosError> {

        val callable = Callable {
            pushTransaction<ProfileMetadataDeleteResult>(CommuntContract.SOCIAL, CommunActions.DELETE_METADATA,
                    MyTransactionAuthorizationAbi(fromAccount),
                    AbiBinaryGenCommun4J(CompressionType.NONE).squishProfileMetadataDeleteRequest(ProfileMetadataDeleteRequest(fromAccount)).toHex(),
                    userActiveKey)
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    fun deleteUserMetadata(): Either<TransactionSuccessful<ProfileMetadataDeleteResult>, GolosEosError> {

        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")
        return deleteUserMetadata(activeAccountName, activeAccountKey)
    }

    fun createComment(fromAccount: CommunName,
                      userActiveKey: String,
                      body: String,
                      parentAccount: CommunName,
                      parentPermlink: String,
                      category: Tag,
                      beneficiaries: List<io.golos.commun4J.model.Beneficiary> = listOf(),
                      vestPayment: Boolean = true,
                      tokenProp: Long = 0L): Either<TransactionSuccessful<CreatePostResult>, GolosEosError> {


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
                                    newJsonMetadata: String): Either<TransactionSuccessful<UpdatePostResult>, GolosEosError> {
        val callable = Callable {
            val updateRequest = UpdatePostRequest(newPostAuthor, newPermlink, newTitle, newBody,
                    newLanguage, newTags, newJsonMetadata)
            pushTransaction<UpdatePostResult>(CommuntContract.PUBLICATION,
                    CommunActions.UPDATE_MESSAGE,
                    MyTransactionAuthorizationAbi(postAuthor.name),
                    createBinaryConverter().squishUpdatePostRequest(updateRequest).toHex(),
                    userActiveKey)
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    fun updatePost(postAuthor: CommunName,
                   userActiveKey: String,
                   newPostAuthor: CommunName,
                   newPermlink: String,
                   newTitle: String,
                   newBody: String,
                   newTags: List<Tag>): Either<TransactionSuccessful<UpdatePostResult>, GolosEosError> {
        return updatePostOrComment(postAuthor, userActiveKey, newPostAuthor, newPermlink, newTitle, newBody, "ru", newTags, "")
    }

    fun updateComment(postAuthor: CommunName,
                      userActiveKey: String,
                      newPostAuthor: CommunName,
                      newPermlink: String,
                      newBody: String,
                      newCategory: Tag): Either<TransactionSuccessful<UpdatePostResult>, GolosEosError> {

        return updatePostOrComment(postAuthor, userActiveKey, newPostAuthor, newPermlink, "", newBody, "ru", listOf(newCategory), "")
    }

    fun updatePost(newPostAuthor: CommunName,
                   newPermlink: String,
                   newTitle: String,
                   newBody: String,
                   newTags: List<Tag>): Either<TransactionSuccessful<UpdatePostResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return updatePostOrComment(activeAccountName, activeAccountKey, newPostAuthor, newPermlink, newTitle, newBody, "ru", newTags, "")
    }

    fun updateComment(newPostAuthor: CommunName,
                      newPermlink: String,
                      newBody: String,
                      newCategory: Tag): Either<TransactionSuccessful<UpdatePostResult>, GolosEosError> {

        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return updatePostOrComment(activeAccountName, activeAccountKey, newPostAuthor, newPermlink, "", newBody, "ru", listOf(newCategory), "")
    }

    fun deletePostOrComment(postAuthor: CommunName,
                            userActiveKey: String,
                            permlink: String): Either<TransactionSuccessful<DeleteResult>, GolosEosError> {
        val callable = Callable {
            pushTransaction<DeleteResult>(CommuntContract.PUBLICATION,
                    CommunActions.DELETE_MESSAGE,
                    MyTransactionAuthorizationAbi(postAuthor),
                    createBinaryConverter().squishDeleteMessageRequest(DeleteMessageRequest(postAuthor, permlink)).toHex(),
                    userActiveKey)
        }

        return callTilTimeoutExceptionVanishes(callable)
    }

    fun deletePostOrComment(permlink: String):
            Either<TransactionSuccessful<DeleteResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return deletePostOrComment(activeAccountName, activeAccountKey, permlink)
    }

    //vote strength -10000 _+10000
    fun vote(postAuthor: CommunName,
             postPermlink: String,
             voteStrength: Short): Either<TransactionSuccessful<VoteResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return vote(activeAccountName, activeAccountKey, postAuthor, postPermlink, voteStrength)
    }

    //vote strength -10000 _+10000
    fun vote(fromAccount: CommunName,
             userActiveKey: String,
             postAuthor: CommunName,
             postPermlink: String,
             voteStrength: Short): Either<TransactionSuccessful<VoteResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val operationHex = if (voteStrength == 0.toShort()) squisher.squishUnVoteRequest(io.golos.commun4J.model.UnVoteRequest(fromAccount, postAuthor, postPermlink)).toHex()
            else squisher.squishVoteRequest(io.golos.commun4J.model.VoteRequest(fromAccount, postAuthor, postPermlink,
                    Math.abs(voteStrength.toInt()).toShort())).toHex()

            pushTransaction<VoteResult>(CommuntContract.PUBLICATION,
                    if (voteStrength == 0.toShort()) CommunActions.UN_VOTE else if (voteStrength > 0) CommunActions.UP_VOTE else CommunActions.DOWN_VOTE,
                    MyTransactionAuthorizationAbi(fromAccount.name),
                    operationHex,
                    userActiveKey)

        }
        return callTilTimeoutExceptionVanishes(callable)

    }

    fun createAccount(newAccountName: String,
                      newAccountMasterPassword: String,
                      eosioCreateUserKey: String): Either<TransactionSuccessful<AccountCreationResult>, GolosEosError> {
        CommunName(newAccountName)
        val creatorAccountName = "eosio"
        val keys = AuthUtils.generatePublicWiFs(newAccountName, newAccountMasterPassword, AuthType.values())

        val callable = Callable {
            val writer = AbiBinaryGenTransactionWriter(CompressionType.NONE)
            val newAccArgs = NewAccountArgs(CommuntContract.EOSIO.toString(),
                    newAccountName,
                    AccountRequiredAuthAbi(1,
                            listOf(AccountKeyAbi(keys[AuthType.OWNER]!!.replaceFirst("GLS", "EOS"), 1)),
                            emptyList(), emptyList()),
                    AccountRequiredAuthAbi(1,
                            listOf(AccountKeyAbi(keys[AuthType.ACTIVE]!!.replaceFirst("GLS", "EOS"), 1)), emptyList(), emptyList()))
            val newAccBody = NewAccountBody(newAccArgs)
            val hex = writer.squishNewAccountBody(newAccBody).toHex()
            pushTransaction<AccountCreationResult>(CommuntContract.EOSIO,
                    CommunActions.NEW_ACCOUNT, MyTransactionAuthorizationAbi(creatorAccountName, "createuser"),
                    hex,
                    eosioCreateUserKey)
        }
        val createAnswer = callTilTimeoutExceptionVanishes(callable)

        val result = startVesting(newAccountName, eosioCreateUserKey)

        if (result is Either.Failure) throw IllegalStateException("error happened during initialization of account," +
                "сall startVesting(newAccountName, eosioCreateUserKey) to fully init new account $newAccountName")
        return createAnswer
    }

    fun startVesting(newAccountName: String,
                     eosioCreateUserKey: String): Either<TransactionSuccessful<Any>, GolosEosError> {
        val creatorAccountName = "eosio"

        val createVestingCallable = Callable {
            val writer = createBinaryConverter()
            val request = VestingStartRequest(CommunName(newAccountName))

            val result = writer.squishVestingStartRequest(request)

            val hex = result.toHex()

            pushTransaction<Any>(CommuntContract.VESTING,
                    CommunActions.OPEN_VESTING, MyTransactionAuthorizationAbi(creatorAccountName, "createuser"),
                    hex,
                    eosioCreateUserKey)
        }

        return callTilTimeoutExceptionVanishes(createVestingCallable)
    }

    fun getDiscussions() = historyApiProvider.getDiscussions()

    fun getDiscussion(id: String) = historyApiProvider.getDiscussion(id)

    private fun createBinaryConverter(): AbiBinaryGenCommun4J {
        return AbiBinaryGenCommun4J(CyberwayByteWriter(), DefaultHexWriter(), CompressionType.NONE)
    }
}