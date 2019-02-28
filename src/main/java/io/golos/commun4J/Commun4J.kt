package io.golos.commun4J

import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.AbiBinaryGenTransactionWriter
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.AccountKeyAbi
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.AccountRequiredAuthAbi
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.NewAccountArgs
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.NewAccountBody
import com.memtrip.eos.chain.actions.transaction.transfer.actions.TransferArgs
import com.memtrip.eos.chain.actions.transaction.transfer.actions.TransferBody
import com.memtrip.eos.core.block.BlockIdDetails
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.hex.DefaultHexWriter
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.info.Info
import com.squareup.moshi.Moshi
import io.golos.commun4J.model.*
import io.golos.commun4J.services.CommunServicesApiService
import io.golos.commun4J.services.model.ApiResponseError
import io.golos.commun4J.utils.AuthUtils
import io.golos.commun4J.utils.CommunNameAdapter
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.checkArgument
import net.gcardone.junidecode.Junidecode
import java.util.concurrent.Callable

internal interface CommunContract {

    fun getActions(): List<CommunContract.CommunAction>

    interface CommunAction
}

private enum class CommunActions : CommunContract.CommunAction {
    CREATE_DISCUSSION, UPDATE_DISCUSSION, DELETE_DISCUSSION, UP_VOTE,
    DOWN_VOTE, UN_VOTE,
    NEW_ACCOUNT, OPEN_VESTING,
    UPDATE_META, DELETE_METADATA, TRANSFER, PIN,
    UN_PIN, BLOCK, UN_BLOCK;

    override fun toString(): String {
        return when (this) {
            CREATE_DISCUSSION -> "createmssg"
            UPDATE_DISCUSSION -> "updatemssg"
            UP_VOTE -> "upvote"
            DOWN_VOTE -> "downvote"
            UN_VOTE -> "unvote"
            DELETE_DISCUSSION -> "deletemssg"
            NEW_ACCOUNT -> "newaccount"
            OPEN_VESTING -> "open"
            UPDATE_META -> "updatemeta"
            DELETE_METADATA -> "deletemeta"
            TRANSFER -> "transfer"
            PIN -> "pin"
            UN_PIN -> "unpin"
            BLOCK -> "block"
            UN_BLOCK -> "unblock"
        }
    }
}

private enum class CommunContracts : CommunContract {
    PUBLICATION, EOSIO, CYBER, VESTING, SOCIAL, TOKEN;

    override fun getActions(): List<CommunContract.CommunAction> {
        return when (this) {
            PUBLICATION -> listOf(CommunActions.CREATE_DISCUSSION,
                    CommunActions.UPDATE_DISCUSSION,
                    CommunActions.DELETE_DISCUSSION,
                    CommunActions.UP_VOTE,
                    CommunActions.DOWN_VOTE,
                    CommunActions.UN_VOTE)

            EOSIO, CYBER -> listOf(CommunActions.NEW_ACCOUNT)

            VESTING -> listOf(CommunActions.OPEN_VESTING)

            SOCIAL -> listOf(CommunActions.UPDATE_META,
                    CommunActions.DELETE_METADATA,
                    CommunActions.PIN,
                    CommunActions.UN_PIN,
                    CommunActions.BLOCK,
                    CommunActions.UN_BLOCK)
            TOKEN -> listOf(CommunActions.TRANSFER)
        }
    }

    override fun toString(): String {
        return when (this) {
            PUBLICATION -> "gls.publish"
            EOSIO -> "eosio"
            CYBER -> "cyber"
            VESTING -> "gls.vesting"
            SOCIAL -> "gls.social"
            TOKEN -> "cyber.token"
        }
    }


}

class Commun4J @JvmOverloads constructor(private val config: io.golos.commun4J.Commun4JConfig = io.golos.commun4J.Commun4JConfig(),
                                         chainApiProvider: io.golos.commun4J.ChainApiProvider? = null,
                                         val keyStorage: CommunKeyStorage = CommunKeyStorage(),
                                         private val apiService: ApiService = CommunServicesApiService(config, keyStorage)) {
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
            this.transactionPusher = io.golos.commun4J.TransactionPusherImpl(chainApi, config, moshi)
        } else {
            this.transactionPusher = io.golos.commun4J.TransactionPusherImpl(chainApiProvider.provide(), config, moshi)
            chainApi = chainApiProvider.provide()
        }
    }

    // create post, using active account from CommunKeyStorage

    fun createPost(title: String,
                   body: String,
                   tags: List<io.golos.commun4J.model.Tag>,
                   beneficiaries: List<io.golos.commun4J.model.Beneficiary> = emptyList(),
                   vestPayment: Boolean = true,
                   tokenProp: Long = 0L): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {

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
                   tokenProp: Long = 0L): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {

        return createPostOrComment(fromAccount, userActiveKey,
                title, body, "${Junidecode.unidecode(title)}-${System.currentTimeMillis()}",
                "", CommunName(), 0L, tags, beneficiaries, vestPayment, tokenProp)
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

    private inline fun <reified T> pushTransaction(contractAccount: CommunContract,
                                                   actionName: CommunContract.CommunAction,
                                                   authorization: MyTransactionAuthorizationAbi,
                                                   data: String,
                                                   key: String,
                                                   prefetchedChainInfo: Info? = null): Either<TransactionSuccessful<T>, GolosEosError> {

        return transactionPusher.pushTransaction(listOf(MyActionAbi(contractAccount.toString(),
                actionName.toString(), listOf(authorization), data)),
                EosPrivateKey(key),
                T::class.java,
                prefetchedChainInfo)
    }

    private fun createPostOrComment(fromAccount: CommunName,
                                    userActiveKey: String,
                                    title: String,
                                    body: String,
                                    permlink: String,
                                    parentPermlink: String,
                                    parentAccount: CommunName,
                                    parentDiscussionRefBlockId: Long,
                                    tags: List<io.golos.commun4J.model.Tag>,
                                    beneficiaries: List<io.golos.commun4J.model.Beneficiary> = emptyList(),
                                    vestPayment: Boolean = true,
                                    tokenProp: Long = 0L): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {

        val callable = Callable<Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError>> {

            val chainInfo = chainApi.getInfo().blockingGet().body()!!


            val createPostRequest = io.golos.commun4J.model.CreateDiscussionRequestAbi(
                    DiscussionIdAbi(fromAccount, permlink, BlockIdDetails(chainInfo.head_block_id).blockNum.toLong()),
                    DiscussionIdAbi(parentAccount, parentPermlink, parentDiscussionRefBlockId),
                    beneficiaries,
                    title,
                    body,
                    tags,
                    tokenProp,
                    vestPayment,
                    "ru",
                    "")

            println("createPostRequest = ${moshi.adapter(CreateDiscussionRequestAbi::class.java).toJson(createPostRequest)}")

            val result = createBinaryConverter().squishCreateDiscussionRequest(createPostRequest)
            pushTransaction(CommunContracts.PUBLICATION, CommunActions.CREATE_DISCUSSION,
                    MyTransactionAuthorizationAbi(fromAccount.name), result.toHex(),
                    userActiveKey,
                    chainInfo)
        }

        return callTilTimeoutExceptionVanishes(callable)
    }


    fun createComment(body: String,
                      parentAccount: CommunName,
                      parentPermlink: String,
                      parentDiscussionRefBlockNum: Long,
                      category: Tag,
                      beneficiaries: List<io.golos.commun4J.model.Beneficiary> = emptyList(),
                      vestPayment: Boolean = true,
                      tokenProp: Long = 0L): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return createComment(activeAccountName,
                activeAccountKey,
                body,
                parentAccount,
                parentPermlink,
                parentDiscussionRefBlockNum,
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
            targetPointB: String? = null): Either<TransactionSuccessful<ProfileMetadataUpdateResult>, GolosEosError> {

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
            targetPointB: String? = null): Either<TransactionSuccessful<ProfileMetadataUpdateResult>, GolosEosError> {

        val callable = Callable {
            val request = ProfileMetadataUpdateRequestAbi(fromAccount,
                    ProfileMetadataAbi(type, app, email, phone, facebook, instagram,
                            telegram, vk, website, first_name, last_name, name, birthDate, gender, location,
                            city, about, occupation, iCan, lookingFor, businessCategory, backgroundImage, coverImage,
                            profileImage, userImage, icoAddress, targetDate, targetPlan, targetPointA, targetPointB))


            val hex = AbiBinaryGenCommun4J(CompressionType.NONE).squishProfileMetadataUpdateRequest(request).toHex()

            pushTransaction<ProfileMetadataUpdateResult>(CommunContracts.SOCIAL,
                    CommunActions.UPDATE_META, MyTransactionAuthorizationAbi(fromAccount),
                    hex, userActiveKey)
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    fun deleteUserMetadata(ofUser: CommunName,
                           userActiveKey: String): Either<TransactionSuccessful<ProfileMetadataDeleteResult>, GolosEosError> {

        val callable = Callable {
            pushTransaction<ProfileMetadataDeleteResult>(CommunContracts.SOCIAL, CommunActions.DELETE_METADATA,
                    MyTransactionAuthorizationAbi(ofUser),
                    AbiBinaryGenCommun4J(CompressionType.NONE).squishProfileMetadataDeleteRequest(ProfileMetadataDeleteRequestAbi(ofUser)).toHex(),
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
                      parentDiscussionRefBlockNum: Long,
                      category: Tag,
                      beneficiaries: List<io.golos.commun4J.model.Beneficiary> = listOf(),
                      vestPayment: Boolean = true,
                      tokenProp: Long = 0L): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {

        checkArgument(parentAccount.name.isNotEmpty(), "parentAccount cannot be empty")
        checkArgument(parentPermlink.isNotEmpty(), "parentPermlink cannot be empty")

        val commentPermlink = "re-${if (parentPermlink.length > 200) parentPermlink.substring(0, 200) else parentPermlink}-${System.currentTimeMillis()}"

        return createPostOrComment(fromAccount, userActiveKey, "", body,
                commentPermlink, parentPermlink,
                parentAccount, parentDiscussionRefBlockNum, listOf(category), beneficiaries, vestPayment, tokenProp)
    }

    private fun updateDiscussion(discussionAuthor: CommunName,
                                 discussionPermlink: String,
                                 discussionRefBlockNum: Long,
                                 userActiveKey: String,
                                 newTitle: String,
                                 newBody: String,
                                 newLanguage: String,
                                 newTags: List<Tag>,
                                 newJsonMetadata: String): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {

        val callable = Callable {
            val updateRequest = UpdateDiscussionRequestAbi(DiscussionIdAbi(discussionAuthor, discussionPermlink, discussionRefBlockNum),
                    newTitle, newBody, newTags,
                    newLanguage, newJsonMetadata)
            pushTransaction<UpdateDiscussionResult>(CommunContracts.PUBLICATION,
                    CommunActions.UPDATE_DISCUSSION,
                    MyTransactionAuthorizationAbi(discussionAuthor.name),
                    createBinaryConverter().squishUpdateDiscussionRequest(updateRequest).toHex(),
                    userActiveKey)
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    fun updatePost(userActiveKey: String,
                   postAuthor: CommunName,
                   postPermlink: String,
                   postRefBlockNum: Long,
                   newTitle: String,
                   newBody: String,
                   newTags: List<Tag>): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {
        return updateDiscussion(postAuthor, postPermlink, postRefBlockNum, userActiveKey, newTitle, newBody, "ru", newTags, "")
    }

    fun updateComment(userActiveKey: String,
                      commentAuthor: CommunName,
                      commentPermlink: String,
                      commentRefBlockNum: Long,
                      newBody: String,
                      newCategory: Tag): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {

        return updateDiscussion(commentAuthor, commentPermlink, commentRefBlockNum, userActiveKey,
                "", newBody, "ru", listOf(newCategory), "")
    }

    fun updatePost(postPermlink: String,
                   postRefBlockNum: Long,
                   newTitle: String,
                   newBody: String,
                   newTags: List<Tag>): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {

        val postAuthor = keyStorage.getActiveAccount()
        val key = (keyStorage
                .getAccountKeys(postAuthor)?.find { it.first == AuthType.ACTIVE }
                ?: throw IllegalStateException("could not find active keys for user $postAuthor")).second

        return updateDiscussion(postAuthor, postPermlink, postRefBlockNum, key, newTitle, newBody, "ru", newTags, "")
    }

    fun updateComment(commentPermlink: String,
                      commentRefBlockNum: Long,
                      newBody: String,
                      newCategory: Tag): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {
        val commentAuthor = keyStorage.getActiveAccount()
        val key = (keyStorage
                .getAccountKeys(commentAuthor)?.find { it.first == AuthType.ACTIVE }
                ?: throw IllegalStateException("could not find active keys for user $commentAuthor")).second

        return updateDiscussion(commentAuthor, commentPermlink, commentRefBlockNum, key, "", newBody, "ru", listOf(newCategory), "")
    }

    fun deletePostOrComment(userActiveKey: String,
                            postOrCommentAuthor: CommunName,
                            postOrCommentPermlink: String,
                            postOrCommentRefBlockNum: Long): Either<TransactionSuccessful<DeleteResult>, GolosEosError> {
        val callable = Callable {
            pushTransaction<DeleteResult>(CommunContracts.PUBLICATION,
                    CommunActions.DELETE_DISCUSSION,
                    MyTransactionAuthorizationAbi(postOrCommentAuthor),
                    createBinaryConverter().squishDeleteDiscussionRequest(DeleteDiscussionRequestAbi(DiscussionIdAbi(postOrCommentAuthor,
                            postOrCommentPermlink,
                            postOrCommentRefBlockNum))).toHex(),
                    userActiveKey)
        }

        return callTilTimeoutExceptionVanishes(callable)
    }

    fun deletePostOrComment(postOrCommentPermlink: String,
                            postOrCommentRefBlockNum: Long):
            Either<TransactionSuccessful<DeleteResult>, GolosEosError> {

        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return deletePostOrComment(activeAccountKey, activeAccountName, postOrCommentPermlink, postOrCommentRefBlockNum)
    }

    //vote strength -10000 _+10000
    fun vote(postOrCommentAuthor: CommunName,
             postOrCommentPermlink: String,
             postOrCommentRefBlockNum: Long,
             voteStrength: Short): Either<TransactionSuccessful<VoteResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")

        return vote(activeAccountName, activeAccountKey, postOrCommentAuthor, postOrCommentPermlink,
                postOrCommentRefBlockNum, voteStrength)
    }

    //vote strength -10000 _+10000
    fun vote(fromAccount: CommunName,
             userActiveKey: String,
             postOrCommentAuthor: CommunName,
             postOrCommentPermlink: String,
             postOrCommentRefBlockNum: Long,
             voteStrength: Short): Either<TransactionSuccessful<VoteResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val discussionId = DiscussionIdAbi(postOrCommentAuthor, postOrCommentPermlink, postOrCommentRefBlockNum)

            val operationHex = if (voteStrength == 0.toShort()) squisher
                    .squishUnVoteRequest(UnVoteRequestAbi(fromAccount, discussionId)).toHex()
            else squisher.squishVoteRequest(VoteRequestAbi(fromAccount, discussionId,
                    Math.abs(voteStrength.toInt()).toShort())).toHex()

            pushTransaction<VoteResult>(CommunContracts.PUBLICATION,
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
        val creatorAccountName = if (config.isPrivateTestNet) CommunContracts.CYBER.toString() else CommunContracts.EOSIO.toString()

        val keys = AuthUtils.generatePublicWiFs(newAccountName, newAccountMasterPassword, AuthType.values())

        val callable = Callable {
            val writer = AbiBinaryGenTransactionWriter(CompressionType.NONE)

            val newAccArgs = NewAccountArgs(if (config.isPrivateTestNet) CommunContracts.CYBER.toString()
            else CommunContracts.EOSIO.toString(),
                    newAccountName,
                    AccountRequiredAuthAbi(1,
                            listOf(AccountKeyAbi(keys[AuthType.OWNER]!!.replaceFirst("GLS", "EOS"), 1)),
                            emptyList(), emptyList()),
                    AccountRequiredAuthAbi(1,
                            listOf(AccountKeyAbi(keys[AuthType.ACTIVE]!!.replaceFirst("GLS", "EOS"), 1)), emptyList(), emptyList()))
            val newAccBody = NewAccountBody(newAccArgs)
            val hex = writer.squishNewAccountBody(newAccBody).toHex()
            pushTransaction<AccountCreationResult>(if (config.isPrivateTestNet) CommunContracts.CYBER else CommunContracts.EOSIO,
                    CommunActions.NEW_ACCOUNT, MyTransactionAuthorizationAbi(creatorAccountName, "createuser"),
                    hex,
                    eosioCreateUserKey)
        }
        val createAnswer = callTilTimeoutExceptionVanishes(callable)

        if (createAnswer is Either.Failure) return createAnswer

        val result = startVesting(newAccountName, eosioCreateUserKey)

        if (result is Either.Failure) throw IllegalStateException("error happened during initialization of account," +
                "сall startVesting(newAccountName, eosioCreateUserKey) to fully init new account $newAccountName")
        return createAnswer
    }

    fun startVesting(newAccountName: String,
                     eosioCreateUserKey: String): Either<TransactionSuccessful<Any>, GolosEosError> {
        val creatorAccountName = if (config.isPrivateTestNet) CommunContracts.CYBER.toString() else CommunContracts.EOSIO.toString()

        val createVestingCallable = Callable {
            val writer = createBinaryConverter()
            val request = VestingStartRequestAbi(CommunName(newAccountName), CommunName(creatorAccountName))

            val result = writer.squishVestingStartRequest(request)

            val hex = result.toHex()

            pushTransaction<Any>(CommunContracts.VESTING,
                    CommunActions.OPEN_VESTING, MyTransactionAuthorizationAbi(creatorAccountName, "createuser"),
                    hex,
                    eosioCreateUserKey)
        }

        return callTilTimeoutExceptionVanishes(createVestingCallable)
    }

    fun getCommunityPosts(communityId: String,
                          limit: Int,
                          sort: DiscussionTimeSort,
                          sequenceKey: String? = null) = apiService.getDiscussions(PostsFeedType.COMMUNITY, sort, sequenceKey, limit, null, communityId)

    fun getUserSubsriptions(user: CommunName,
                            limit: Int,
                            sort: DiscussionTimeSort,
                            sequenceKey: String?) = apiService.getDiscussions(PostsFeedType.SUBSCRIPTIONS,
            sort, sequenceKey, limit, user.name, null)

    fun getUserPosts(user: CommunName,
                     limit: Int,
                     sort: DiscussionTimeSort,
                     sequenceKey: String? = null) =
            apiService.getDiscussions(PostsFeedType.USER_POSTS, sort, sequenceKey, limit, user.name, null)


    fun getPost(user: CommunName,
                permlink: String,
                refBlockNum: Int) = apiService.getDiscussion(user.name, permlink, refBlockNum)

    fun getCommentsOfPost(user: CommunName,
                          permlink: String,
                          refBlockNum: Int?,
                          limit: Int,
                          sort: DiscussionTimeSort,
                          sequenceKey: String? = null) =

            apiService.getComments(sort, sequenceKey, limit,
                    CommentsOrigin.COMMENTS_OF_POST, user.name, permlink, refBlockNum)

    fun getCommentsOfUser(user: CommunName,
                          limit: Int,
                          sort: DiscussionTimeSort,
                          sequenceKey: String? = null): Either<DiscussionsResult, ApiResponseError> =
            apiService.getComments(sort, sequenceKey, limit,
                    CommentsOrigin.COMMENTS_OF_USER, user.name, null, null)


    fun getUserMetadata(user: CommunName): Either<UserMetadata, ApiResponseError> = apiService.getUserMetadata(user.name)

    fun addAuthListener(listener: AuthListener){
        apiService.addOnAuthListener(listener)
    }


    fun transfer(key: String,
                 from: CommunName,
                 to: CommunName,
                 amount: String,
                 currency: String,
                 memo: String = ""): Either<TransactionSuccessful<TransferResult>, GolosEosError> {

        if (!amount.matches("([0-9]+\\.[0-9]{3})".toRegex())) throw IllegalArgumentException("wrong currency format. Must have 3 points precision, like 12.000 or 0.001")

        val callable = Callable {
            val hex = AbiBinaryGenTransactionWriter(CyberwayByteWriter(), DefaultHexWriter(), CompressionType.NONE)
                    .squishTransferBody(TransferBody(TransferArgs(from.name, to.name, "$amount $currency", memo))).toHex()
            pushTransaction<TransferResult>(CommunContracts.TOKEN, CommunActions.TRANSFER,
                    MyTransactionAuthorizationAbi(from), hex, key)
        }

        return callTilTimeoutExceptionVanishes(callable)
    }

    fun transfer(to: CommunName,
                 amount: String,
                 currency: String,
                 memo: String = ""): Either<TransactionSuccessful<TransferResult>, GolosEosError> {
        return transfer(keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }!!.second,
                keyStorage.getActiveAccount(),
                to,
                amount,
                currency,
                memo)
    }

    fun pin(pinning: CommunName): Either<TransactionSuccessful<PinResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")
        return pin(activeAccountKey, activeAccountName, pinning)
    }

    fun pin(activeKey: String,
            pinner: CommunName,
            pinning: CommunName): Either<TransactionSuccessful<PinResult>, GolosEosError> {

        val callable = Callable {
            val hex = createBinaryConverter().squishPinRequest(PinRequestAbi(pinner, pinning)).toHex()
            pushTransaction<PinResult>(CommunContracts.SOCIAL,
                    CommunActions.PIN,
                    pinner.toTransactionAuthAbi(),
                    hex,
                    activeKey)
        }
        return callTilTimeoutExceptionVanishes(callable = callable)
    }

    fun unPin(pinning: CommunName): Either<TransactionSuccessful<PinResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")
        return unPin(activeAccountKey, activeAccountName, pinning)
    }

    fun unPin(activeKey: String,
              pinner: CommunName,
              pinning: CommunName): Either<TransactionSuccessful<PinResult>, GolosEosError> {

        val callable = Callable {
            val hex = createBinaryConverter().squishPinRequest(PinRequestAbi(pinner, pinning)).toHex()
            pushTransaction<PinResult>(CommunContracts.SOCIAL,
                    CommunActions.UN_PIN,
                    pinner.toTransactionAuthAbi(),
                    hex,
                    activeKey)
        }
        return callTilTimeoutExceptionVanishes(callable = callable)
    }

    fun block(user: CommunName): Either<TransactionSuccessful<BlockUserResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")
        return block(activeAccountKey, activeAccountName, user)
    }

    fun block(blockerActiveKey: String,
              blocker: CommunName,
              blocking: CommunName): Either<TransactionSuccessful<BlockUserResult>, GolosEosError> {

        val callable = Callable {
            pushTransaction<BlockUserResult>(CommunContracts.SOCIAL, CommunActions.BLOCK,
                    blocker.toTransactionAuthAbi(),
                    createBinaryConverter().squishBlockUserRequest(BlockUserRequestAbi(blocker, blocking)).toHex(),
                    blockerActiveKey)
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    fun unBlock(user: CommunName): Either<TransactionSuccessful<BlockUserResult>, GolosEosError> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")
        return unBlock(activeAccountKey, activeAccountName, user)
    }

    fun unBlock(blockerActiveKey: String,
                blocker: CommunName,
                blocking: CommunName): Either<TransactionSuccessful<BlockUserResult>, GolosEosError> {

        val callable = Callable {
            pushTransaction<BlockUserResult>(CommunContracts.SOCIAL, CommunActions.UN_BLOCK,
                    blocker.toTransactionAuthAbi(),
                    createBinaryConverter().squishBlockUserRequest(BlockUserRequestAbi(blocker, blocking)).toHex(),
                    blockerActiveKey)
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

}

private fun CommunName.toTransactionAuthAbi(): MyTransactionAuthorizationAbi = MyTransactionAuthorizationAbi(this.name)

private fun createBinaryConverter(): AbiBinaryGenCommun4J {
    return AbiBinaryGenCommun4J(CyberwayByteWriter(), DefaultHexWriter(), CompressionType.NONE)
}