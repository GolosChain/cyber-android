@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.golos.cyber4j

import com.memtrip.eos.abi.writer.bytewriter.DefaultByteWriter
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.AbiBinaryGenTransactionWriter
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.AccountKeyAbi
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.AccountRequiredAuthAbi
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.NewAccountArgs
import com.memtrip.eos.chain.actions.transaction.account.actions.newaccount.NewAccountBody
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.hex.DefaultHexWriter
import com.memtrip.eos.http.rpc.model.account.request.AccountName
import com.memtrip.eos.http.rpc.model.info.Info
import com.squareup.moshi.Moshi
import io.golos.annotations.ExcludeFromGeneration
import io.golos.annotations.GenerateCoroutinesAdapter
import io.golos.annotations.ShutDownMethod
import io.golos.cyber4j.model.*
import io.golos.cyber4j.services.CyberServicesApiService
import io.golos.cyber4j.services.model.*
import io.golos.cyber4j.utils.*
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.CyberNameAdapter
import net.gcardone.junidecode.Junidecode
import java.io.File
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList

private interface CyberContract {

    fun getActions(): List<CyberContract.CyberAction>

    interface CyberAction
}

private enum class CyberActions : CyberContract.CyberAction {
    CREATE_DISCUSSION, UPDATE_DISCUSSION, DELETE_DISCUSSION, UP_VOTE,
    DOWN_VOTE, UN_VOTE,
    NEW_ACCOUNT, OPEN_VESTING,
    UPDATE_META, DELETE_METADATA, TRANSFER, PIN,
    UN_PIN, BLOCK, UN_BLOCK,
    ISSUE, REBLOG, VOTE_FOR_WITNESS, UNVOTE_WITNESS,
    REGISTER_WITNESS, UNREGISTER_WITNESS, START_WITNESS, STOP_WITNESS,
    SET_NEW_USER_NAME;

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
            ISSUE -> "issue"
            REBLOG -> "reblog"
            VOTE_FOR_WITNESS -> "votewitness"
            UNVOTE_WITNESS -> "unvotewitn"
            REGISTER_WITNESS -> "regwitness"
            UNREGISTER_WITNESS -> "unregwitness"
            START_WITNESS -> "startwitness"
            STOP_WITNESS -> "stopwitness"
            SET_NEW_USER_NAME -> "newusername"
        }
    }
}

private enum class CyberContracts : CyberContract {
    PUBLICATION, GLS, CYBER, VESTING, SOCIAL, TOKEN, CYBER_TOKEN, CTRL, DOMAIN;

    override fun getActions(): List<CyberContract.CyberAction> {
        return when (this) {
            PUBLICATION -> listOf(
                    CyberActions.CREATE_DISCUSSION,
                    CyberActions.UPDATE_DISCUSSION,
                    CyberActions.DELETE_DISCUSSION,
                    CyberActions.UP_VOTE,
                    CyberActions.DOWN_VOTE,
                    CyberActions.UN_VOTE,
                    CyberActions.REBLOG
            )
            DOMAIN -> listOf(CyberActions.SET_NEW_USER_NAME)

            GLS -> listOf(CyberActions.NEW_ACCOUNT)

            VESTING -> listOf(CyberActions.OPEN_VESTING)

            SOCIAL -> listOf(
                    CyberActions.UPDATE_META,
                    CyberActions.DELETE_METADATA,
                    CyberActions.PIN,
                    CyberActions.UN_PIN,
                    CyberActions.BLOCK,
                    CyberActions.UN_BLOCK
            )
            CYBER -> listOf()
            TOKEN -> listOf(CyberActions.TRANSFER)
            CYBER_TOKEN -> listOf(CyberActions.ISSUE, CyberActions.OPEN_VESTING)
            CTRL -> listOf(CyberActions.VOTE_FOR_WITNESS, CyberActions.UNVOTE_WITNESS,
                    CyberActions.START_WITNESS, CyberActions.STOP_WITNESS)
        }
    }

    override fun toString(): String {
        return when (this) {
            PUBLICATION -> "gls.publish"
            GLS -> "gls"
            CYBER_TOKEN -> "cyber.token"
            VESTING -> "gls.vesting"
            SOCIAL -> "gls.social"
            TOKEN -> "cyber.token"
            CTRL -> "gls.ctrl"
            DOMAIN -> "cyber.domain"
            CYBER -> "cyber"
        }
    }
}

@GenerateCoroutinesAdapter
class Cyber4J @JvmOverloads constructor(
        config: io.golos.cyber4j.Cyber4JConfig = io.golos.cyber4j.Cyber4JConfig(),
        chainApiProvider: io.golos.cyber4j.ChainApiProvider? = null,
        keyStorage: KeyStorage = KeyStorage(),
        private val apiService: ApiService = CyberServicesApiService(config)) {

    private val staleTransactionErrorCode = 3080006
    private val transactionPusher: io.golos.cyber4j.TransactionPusher
    private val chainApi: CyberWayChainApi
    private val moshi: Moshi = Moshi
            .Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .add(CyberName::class.java, CyberNameAdapter())
            .build()

    val keyStorage = keyStorage
        @ExcludeFromGeneration get
    val config = config
        @ExcludeFromGeneration get


    init {
        if (chainApiProvider == null) {
            chainApi = io.golos.cyber4j.GolosEosConfiguratedApi(config, moshi).provide()
            this.transactionPusher = io.golos.cyber4j.TransactionPusherImpl(chainApi, config, moshi)
        } else {
            this.transactionPusher = io.golos.cyber4j.TransactionPusherImpl(chainApiProvider.provide(), config, moshi)
            chainApi = chainApiProvider.provide()
        }

    }

    /** method for creating post, using active credentials from [keyStorage]
     *
     * @param title title of post. Currently must be fewer, then 256 symbols
     * @param body body test of post. Currently must be not empty
     * @param tags tags list for post. Must be not empty
     * @param metadata metadata of a post. Can be empty
     * @param beneficiaries beneficiaries of a post. Can be empty
     * @param curatorRewardPercentage curation reward percentage, 0..10_000
     * @param vestPayment true to allow vestPayment of author to for a post
     * @param tokenProp idk
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     *
     */

    fun createPost(
            title: String,
            body: String,
            tags: List<io.golos.cyber4j.model.Tag>,
            metadata: DiscussionCreateMetadata,
            curatorRewardPercentage: Short?,
            beneficiaries: List<io.golos.cyber4j.model.Beneficiary> = emptyList(),
            vestPayment: Boolean = true,
            tokenProp: Long = 0L
    ): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {

        val activeUser = resolveKeysFor(CyberContracts.PUBLICATION, CyberActions.CREATE_DISCUSSION)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return createPost(
                activeAccountName,
                activeAccountKey,
                title,
                body,
                tags,
                metadata,
                curatorRewardPercentage,
                beneficiaries,
                vestPayment,
                tokenProp
        )
    }

    /** method for creating post
     * @param fromAccount used account as authority and author
     * @param userActiveKey active key of [fromAccount]
     * @param title title of post. Currently must be fewer, then 256 symbols
     * @param body body test of post. Currently must be not empty
     * @param tags tags list for post. Must be not empty
     * @param beneficiaries beneficiaries of a post. Can be empty
     * @param metadata metadata of a post. Can be empty
     * @param curatorRewardPercentage percentage of curation reward, 0..10_000
     * @param vestPayment true to allow vestPayment of author to for a post
     * @param tokenProp idk
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     *
     */

    fun createPost(
            fromAccount: CyberName,
            userActiveKey: String,
            title: String,
            body: String,
            tags: List<io.golos.cyber4j.model.Tag>,
            metadata: DiscussionCreateMetadata,
            curatorRewardPercentage: Short?,
            beneficiaries: List<io.golos.cyber4j.model.Beneficiary> = emptyList(),
            vestPayment: Boolean = true,
            tokenProp: Long = 0L
    ): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {

        return createPostOrComment(
                fromAccount, userActiveKey,
                title, body, formatPostPermlink(title),
                "", CyberName(), tags, curatorRewardPercentage, beneficiaries, metadata, vestPayment, tokenProp
        )
    }

    private fun isStaleError(callResult: Either<out Any?, GolosEosError>): Boolean {
        return callResult is Either.Failure
                && (callResult.value.error.code == staleTransactionErrorCode)
    }

    private fun formatPostPermlink(permlinkToFormat: String): String {
        val workingCopy =
                if (permlinkToFormat.length < 12) permlinkToFormat + UUID.randomUUID().toString() else permlinkToFormat
        var unicodePermlink = Junidecode.unidecode(workingCopy).toLowerCase().replace(Regex("((?![a-z0-9-]).)"), "")
        if (unicodePermlink.length < 12) unicodePermlink += (UUID.randomUUID().toString().toLowerCase())
        if (unicodePermlink.length > 256) unicodePermlink.substring(0, 257)
        return unicodePermlink
    }

    // sometimes blockain refuses to transact proper transaction due to some inner problems.
    // It return TimeoutException - and i try to push transaction again
    private fun <T> callTilTimeoutExceptionVanishes(
            callable: Callable<Either<TransactionSuccessful<T>,
                    GolosEosError>>
    ): Either<TransactionSuccessful<T>, GolosEosError> {
        var result: Either<TransactionSuccessful<T>, GolosEosError>
        do {
            result = callable.call()
        } while (isStaleError(result))

        return result
    }

    /**function tries to resolve canonical name from domain (..@golos for example) or username
     * @param name userName to resolve to
     * @param appName app in which domain this name is. Currently there is 'cyber' and 'gls' apps
     * @throws IllegalArgumentException if name doesn't exist
     * */
    fun resolveCanonicalCyberName(name: String,
                                  appName: String) =
            apiService.resolveProfile(name, appName)

    /**
     * @hide
     * helper method for creating [MyActionAbi] and return properly typed response
     * */
    private inline fun <reified T> pushTransaction(
            contractAccount: CyberContract,
            actionName: CyberContract.CyberAction,
            authorization: MyTransactionAuthorizationAbi,
            data: String,
            key: String,
            prefetchedChainInfo: Info? = null
    ): Either<TransactionSuccessful<T>, GolosEosError> {

        return transactionPusher.pushTransaction(
                listOf(
                        MyActionAbi(
                                contractAccount.toString(),
                                actionName.toString(), listOf(authorization), data
                        )
                ),
                EosPrivateKey(key),
                T::class.java,
                prefetchedChainInfo
        )
    }

    /**
     * @hide method for creating message  (mssg) structure in cyberway blockchain
     * @see createPost
     * @see createComment
     * */
    private fun createPostOrComment(
            fromAccount: CyberName,
            userActiveKey: String,
            title: String,
            body: String,
            permlink: String,
            parentPermlink: String,
            parentAccount: CyberName,
            tags: List<io.golos.cyber4j.model.Tag>,
            curatorRewardPercentage: Short?,
            beneficiaries: List<io.golos.cyber4j.model.Beneficiary> = emptyList(),
            metadata: DiscussionCreateMetadata = DiscussionCreateMetadata(emptyList(), emptyList()),
            vestPayment: Boolean = true,
            tokenProp: Long = 0L
    ): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {

        val callable = Callable<Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError>> {

            val createPostRequest = io.golos.cyber4j.model.CreateDiscussionRequestAbi(
                    DiscussionIdAbi(
                            fromAccount,
                            permlink),
                    DiscussionIdAbi(parentAccount, parentPermlink),
                    beneficiaries,
                    title,
                    body,
                    tags,
                    tokenProp,
                    vestPayment,
                    "ru",
                    moshi.adapter(DiscussionCreateMetadata::class.java).toJson(metadata),
                    curatorRewardPercentage
            )

            if (config.logLevel == LogLevel.BODY) config.httpLogger?.log(
                    "createPostRequest = ${moshi.adapter(CreateDiscussionRequestAbi::class.java).toJson(
                            createPostRequest
                    )}"
            )

            val result = createBinaryConverter().squishCreateDiscussionRequestAbi(createPostRequest)
            pushTransaction(
                    CyberContracts.PUBLICATION, CyberActions.CREATE_DISCUSSION,
                    MyTransactionAuthorizationAbi(fromAccount.name), result.toHex(),
                    userActiveKey,
                    null
            )
        }

        return callTilTimeoutExceptionVanishes(callable)
    }


    /** method for creating comment, using active credentials from [keyStorage]
     * @param body body of a comment. Must be not empty
     * @param parentAccount user name of author of parent post. must be not blank
     * @param parentPermlink parentPermlink of parent post. must be not blank
     * @param categories categories (tags) of a comment
     * @param metadata metadata of a comment. Can be empty
     * @param beneficiaries beneficiaries of a post. Can be empty
     * @param curatorRewardPercentage percentage of curation reward, 0..10_000
     * @param vestPayment true to allow vestPayment of author to for a post
     * @param tokenProp idk
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     *
     */
    fun createComment(
            body: String,
            parentAccount: CyberName,
            parentPermlink: String,
            categories: List<Tag>,
            metadata: DiscussionCreateMetadata,
            curatorRewardPercentage: Short?,
            beneficiaries: List<io.golos.cyber4j.model.Beneficiary> = emptyList(),
            vestPayment: Boolean = true,
            tokenProp: Long = 0L
    ): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.PUBLICATION, CyberActions.CREATE_DISCUSSION)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return createComment(
                activeAccountName,
                activeAccountKey,
                body,
                parentAccount,
                parentPermlink,
                categories,
                metadata,
                curatorRewardPercentage,
                beneficiaries,
                vestPayment,
                tokenProp
        )
    }

    /***
     * method for setting metadata of active user from [keyStorage]
     * null in any field means skipping property (i.e field will not changed), while blank string means property erasure
     * field names are self-describing
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */

    fun setUserMetadata(
            type: String? = null,
            app: String? = null,
            email: String? = null,
            phone: String? = null,
            facebook: String? = null,
            instagram: String? = null,
            telegram: String? = null,
            vk: String? = null,
            whatsApp: String? = null,
            weChat: String? = null,
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
            targetPointB: String? = null
    ): Either<TransactionSuccessful<ProfileMetadataUpdateResult>, GolosEosError> {

        val activeUser = resolveKeysFor(CyberContracts.SOCIAL, CyberActions.UPDATE_META)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return setUserMetadata(
                activeAccountName, activeAccountKey, type, app, email, phone,
                facebook, instagram, telegram, vk, whatsApp, weChat, website, first_name, last_name, name, birthDate, gender,
                location, city, about, occupation, iCan, lookingFor, businessCategory, backgroundImage,
                coverImage, profileImage, userImage, icoAddress, targetDate, targetPlan, targetPointA,
                targetPointB
        )
    }

    /***
     * method for setting metadata of [fromAccount] user, using [userActiveKey] eos active key
     * null in any field means erasure of corresponding property, not update
     * field names are self-describing
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */

    fun setUserMetadata(
            fromAccount: CyberName,
            userActiveKey: String,
            type: String? = null,
            app: String? = null,
            email: String? = null,
            phone: String? = null,
            facebook: String? = null,
            instagram: String? = null,
            telegram: String? = null,
            vk: String? = null,
            whatsApp: String? = null,
            weChat: String? = null,
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
            targetPointB: String? = null
    ): Either<TransactionSuccessful<ProfileMetadataUpdateResult>, GolosEosError> {

        val callable = Callable {
            val request = ProfileMetadataUpdateRequestAbi(
                    fromAccount,
                    ProfileMetadataAbi(
                            type, app, email, phone, facebook, instagram,
                            telegram, vk, whatsApp, weChat, website, first_name, last_name, name, birthDate, gender, location,
                            city, about, occupation, iCan, lookingFor, businessCategory, backgroundImage, coverImage,
                            profileImage, userImage, icoAddress, targetDate, targetPlan, targetPointA, targetPointB
                    )
            )


            val hex = createBinaryConverter().squishProfileMetadataUpdateRequestAbi(request).toHex()

            pushTransaction<ProfileMetadataUpdateResult>(
                    CyberContracts.SOCIAL,
                    CyberActions.UPDATE_META, MyTransactionAuthorizationAbi(fromAccount),
                    hex, userActiveKey
            )
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    /***
     * method for deleting metadata of [ofUser] user, using [userActiveKey] eos active key
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */
    fun deleteUserMetadata(
            ofUser: CyberName,
            userActiveKey: String
    ): Either<TransactionSuccessful<ProfileMetadataDeleteResult>, GolosEosError> {

        val callable = Callable {
            pushTransaction<ProfileMetadataDeleteResult>(
                    CyberContracts.SOCIAL, CyberActions.DELETE_METADATA,
                    MyTransactionAuthorizationAbi(ofUser),
                    createBinaryConverter().squishProfileMetadataDeleteRequestAbi(ProfileMetadataDeleteRequestAbi(ofUser)).toHex(),
                    userActiveKey
            )
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    /***
     * method for deleting metadata of active user from [keyStorage]
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */
    fun deleteUserMetadata(): Either<TransactionSuccessful<ProfileMetadataDeleteResult>, GolosEosError> {

        val activeUser = resolveKeysFor(CyberContracts.SOCIAL, CyberActions.DELETE_METADATA)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return deleteUserMetadata(activeAccountName, activeAccountKey)
    }

    /** method for creating comment
     *
     * @param fromAccount used account as authority and author
     * @param userActiveKey active key of [fromAccount]
     * @param body body test of post. Must be not empty
     * @param parentAccount user name of author of parent post. Must be not blank
     * @param parentPermlink parentPermlink of parent post. Must be not blank
     * @param categories list of tags of comments
     * @param curatorRewardPercentage percentage of curation reward, 0..10_000
     * @param metadata metadata of a comment. Can be empty
     * @param beneficiaries beneficiaries of a post. Can be empty
     * @param vestPayment true to allow vestPayment of author to for a post
     * @param tokenProp idk
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun createComment(
            fromAccount: CyberName,
            userActiveKey: String,
            body: String,
            parentAccount: CyberName,
            parentPermlink: String,
            categories: List<Tag>,
            metadata: DiscussionCreateMetadata,
            curatorRewardPercentage: Short?,
            beneficiaries: List<io.golos.cyber4j.model.Beneficiary> = listOf(),
            vestPayment: Boolean = true,
            tokenProp: Long = 0L
    ): Either<TransactionSuccessful<CreateDiscussionResult>, GolosEosError> {

        checkArgument(parentAccount.name.isNotEmpty(), "parentAccount cannot be empty")
        checkArgument(parentPermlink.isNotEmpty(), "parentPermlink cannot be empty")

        val commentPermlink = "re-${if (parentPermlink.length > 200) parentPermlink.substring(
                0,
                200
        ) else parentPermlink}-${System.currentTimeMillis()}"

        return createPostOrComment(
                fromAccount,
                userActiveKey,
                "",
                body,
                commentPermlink,
                parentPermlink,
                parentAccount,
                categories,
                curatorRewardPercentage,
                beneficiaries,
                metadata,
                vestPayment,
                tokenProp
        )
    }

    /** @hide method for updating post or comment
     */

    private fun updateDiscussion(
            discussionAuthor: CyberName,
            discussionPermlink: String,
            userActiveKey: String,
            newTitle: String,
            newBody: String,
            newLanguage: String,
            newTags: List<Tag>,
            newJsonMetadata: DiscussionCreateMetadata
    ): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {

        val callable = Callable {
            val updateRequest = UpdateDiscussionRequestAbi(
                    DiscussionIdAbi(discussionAuthor, discussionPermlink),
                    newTitle, newBody, newTags,
                    newLanguage, moshi.adapter(DiscussionCreateMetadata::class.java).toJson(newJsonMetadata)
            )
            pushTransaction<UpdateDiscussionResult>(
                    CyberContracts.PUBLICATION,
                    CyberActions.UPDATE_DISCUSSION,
                    MyTransactionAuthorizationAbi(discussionAuthor.name),
                    createBinaryConverter().squishUpdateDiscussionRequestAbi(updateRequest).toHex(),
                    userActiveKey
            )
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    /** method updating post
     * @param postAuthor used account as authority and author
     * @param userActiveKey active key of [postAuthor]
     * @param postPermlink of post to update
     * @param newTitle new title of a post. Currently must be fewer, then 256 symbols
     * @param newBody new body a of post. Must be not blank
     * @param newTags new tags a of post
     * @param newJsonMetadata updated  metadata of a post
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun updatePost(
            userActiveKey: String,
            postAuthor: CyberName,
            postPermlink: String,
            newTitle: String,
            newBody: String,
            newTags: List<Tag>,
            newJsonMetadata: DiscussionCreateMetadata
    ): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {
        return updateDiscussion(
                postAuthor,
                postPermlink,
                userActiveKey,
                newTitle,
                newBody,
                "ru",
                newTags,
                newJsonMetadata
        )
    }

    /** method updating comment
     * @param userActiveKey active key pf [commentAuthor]
     * @param commentAuthor author of original comment
     * @param commentPermlink permlink of comment
     * @param newBody new body a of post. Must be not blank
     * @param categories new categories of comment.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun updateComment(
            userActiveKey: String,
            commentAuthor: CyberName,
            commentPermlink: String,
            newBody: String,
            categories: List<Tag>,
            newJsonMetadata: DiscussionCreateMetadata
    ): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {

        return updateDiscussion(
                commentAuthor, commentPermlink, userActiveKey,
                "", newBody, "ru", categories, newJsonMetadata
        )
    }

    /** method updating post, using credential of active account from [keyStorage]
     * @param postPermlink of post to update
     * @param newTitle new title of a post. Currently must be fewer, then 256 symbols
     * @param newBody new body a of post. Must be not blank
     * @param newTags new tags a of post
     * @param newJsonMetadata updated metadata of a post
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */

    fun updatePost(postPermlink: String,
                   newTitle: String,
                   newBody: String,
                   newTags: List<Tag>,
                   newJsonMetadata: DiscussionCreateMetadata
    ): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {

        val activeUser = resolveKeysFor(CyberContracts.PUBLICATION, CyberActions.UPDATE_DISCUSSION)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return updateDiscussion(
                activeAccountName,
                postPermlink,
                activeAccountKey,
                newTitle,
                newBody,
                "ru",
                newTags,
                newJsonMetadata
        )
    }

    /** method updating comment using credentials of active account from [keyStorage]
     * @param commentPermlink permlink of comment.
     * @param newBody new body a of post. Must be not blank
     * @param newCategories new list of categories of a comment
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */
    fun updateComment(
            commentPermlink: String,
            newBody: String,
            newCategories: List<Tag>,
            newJsonMetadata: DiscussionCreateMetadata
    ): Either<TransactionSuccessful<UpdateDiscussionResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.PUBLICATION, CyberActions.UPDATE_DISCUSSION)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return updateDiscussion(
                activeAccountName,
                commentPermlink,
                activeAccountKey,
                "",
                newBody,
                "ru",
                newCategories,
                newJsonMetadata
        )
    }

    /** method deletion post of comment
     * @param userActiveKey active key of [postOrCommentAuthor]
     * @param postOrCommentPermlink permlink of entity to delete
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun deletePostOrComment(
            userActiveKey: String,
            postOrCommentAuthor: CyberName,
            postOrCommentPermlink: String
    ): Either<TransactionSuccessful<DeleteResult>, GolosEosError> {
        val callable = Callable {
            pushTransaction<DeleteResult>(
                    CyberContracts.PUBLICATION,
                    CyberActions.DELETE_DISCUSSION,
                    MyTransactionAuthorizationAbi(postOrCommentAuthor),
                    createBinaryConverter().squishDeleteDiscussionRequestAbi(
                            DeleteDiscussionRequestAbi(
                                    DiscussionIdAbi(
                                            postOrCommentAuthor,
                                            postOrCommentPermlink
                                    )
                            )
                    ).toHex(),
                    userActiveKey
            )
        }

        return callTilTimeoutExceptionVanishes(callable)
    }

    /** method deletion post of comment, using credentials of active account from [keyStorage]
     * @param postOrCommentPermlink permlink of entity to delete
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */
    fun deletePostOrComment(
            postOrCommentPermlink: String):
            Either<TransactionSuccessful<DeleteResult>, GolosEosError> {

        val activeUser = resolveKeysFor(CyberContracts.PUBLICATION, CyberActions.DELETE_DISCUSSION)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return deletePostOrComment(activeAccountKey, activeAccountName, postOrCommentPermlink)
    }


    /** method reblogging post or comment
     * This method assumes that you have added account with keys to [keyStorage]
     * @param authorOfPostToReblog author of entity to reblog
     * @param permlinkOfPostToReblog permlink of entity to reblog
     * @param title idk
     * @param body idk
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */

    fun reblog(
            authorOfPostToReblog: CyberName,
            permlinkOfPostToReblog: String,
            title: String,
            body: String
    ): Either<TransactionSuccessful<ReblogResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.SOCIAL, CyberActions.REBLOG)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return reblog(
                activeAccountKey,
                activeAccountName,
                authorOfPostToReblog,
                permlinkOfPostToReblog,
                title,
                body
        )
    }


    /** method reblogging post or comment
     * @param userActiveKey active key of perso, who want to reblog [authorOfPostToReblog]'s entity
     * @param reblogger name of a person, who want to reblog entity
     * @param authorOfPostToReblog author of entity to reblog
     * @param title idk
     * @param body idk
     * @param permlinkOfPostToReblog permlink of entity to reblog
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun reblog(
            userActiveKey: String,
            reblogger: CyberName,
            authorOfPostToReblog: CyberName,
            permlinkOfPostToReblog: String,
            title: String,
            body: String
    ): Either<TransactionSuccessful<ReblogResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val reblogRequest = ReblogRequestAbi(
                    reblogger,
                    DiscussionIdAbi(
                            authorOfPostToReblog,
                            permlinkOfPostToReblog
                    ), title, body
            )

            val operationHex = squisher.squishReblogRequestAbi(reblogRequest).toHex()

            pushTransaction<ReblogResult>(
                    CyberContracts.PUBLICATION,
                    CyberActions.REBLOG,
                    MyTransactionAuthorizationAbi(reblogger.name),
                    operationHex,
                    userActiveKey
            )

        }
        return callTilTimeoutExceptionVanishes(callable)

    }

    /** vote for a witness
     * @param userActiveKey active key of perso, who want to vote for a [witness]
     * @param voter name of a person, who wants to vote
     * @param witness name of witness to vote to
     *  * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun voteForAWitness(
            userActiveKey: String,
            voter: CyberName,
            witness: CyberName
    ): Either<TransactionSuccessful<WitnessVoteResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val voteRequest = WitnessVoteRequestAbi(
                    voter, witness
            )

            val operationHex = squisher.squishWitnessVoteRequestAbi(voteRequest).toHex()

            pushTransaction<WitnessVoteResult>(
                    CyberContracts.CTRL,
                    CyberActions.VOTE_FOR_WITNESS,
                    MyTransactionAuthorizationAbi(voter.name),
                    operationHex,
                    userActiveKey
            )

        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    /** vote for a witness
     * This method assumes that you have added account with keys to [keyStorage]
     * @param witness name of witness to vote to
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */

    fun voteForAWitness(
            witness: CyberName
    ): Either<TransactionSuccessful<WitnessVoteResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.CTRL, CyberActions.VOTE_FOR_WITNESS)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return voteForAWitness(activeAccountKey, activeAccountName, witness)
    }

    /** cancel vote for a witness
     * @param userActiveKey active key of perso, who want to vote for a [witness]
     * @param voter name of a person, who wants to vote
     * @param witness name of witness to vote to
     *  * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun unVoteForAWitness(
            userActiveKey: String,
            voter: CyberName,
            witness: CyberName
    ): Either<TransactionSuccessful<WitnessVoteResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val voteRequest = WitnessUnVoteRequestAbi(
                    voter, witness
            )

            val operationHex = squisher.squishWitnessUnVoteRequestAbi(voteRequest).toHex()

            pushTransaction<WitnessVoteResult>(
                    CyberContracts.CTRL,
                    CyberActions.UNVOTE_WITNESS,
                    MyTransactionAuthorizationAbi(voter.name),
                    operationHex,
                    userActiveKey
            )

        }
        return callTilTimeoutExceptionVanishes(callable)
    }


    /** cancel vote for a witness
     * This method assumes that you have added account with keys to [keyStorage]
     * @param witness name of witness to vote to
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */

    fun unVoteForAWitness(
            witness: CyberName
    ): Either<TransactionSuccessful<WitnessVoteResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.CTRL, CyberActions.UNVOTE_WITNESS)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return unVoteForAWitness(activeAccountKey, activeAccountName, witness)
    }

    /** register a witness
     * @param userActiveKey active key of a [witness]
     * @param websiteUrl url of [witness] proposals
     * @param witness name of witness who's [userActiveKey] you provide
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun registerAWitness(
            userActiveKey: String,
            witness: CyberName,
            websiteUrl: String
    ): Either<TransactionSuccessful<Any>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val witnessRegisterRequest = RegWitnessRequestAbi(
                    witness,
                    websiteUrl
            )


            val operationHex = squisher.squishRegWitnessRequestAbi(witnessRegisterRequest).toHex()

            pushTransaction<Any>(
                    CyberContracts.CTRL,
                    CyberActions.REGISTER_WITNESS,
                    MyTransactionAuthorizationAbi(witness.name),
                    operationHex,
                    userActiveKey
            )

        }
        return callTilTimeoutExceptionVanishes(callable)
    }


    /** register a witness. This method assumes that you have active account in [keyStorage]. method
     * will try to create witness of active account
     * @param websiteUrl url of proposals of active account as witness
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     *@throws IllegalStateException if active account not set*/


    fun registerAWitness(websiteUrl: String): Either<TransactionSuccessful<Any>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.CTRL, CyberActions.REGISTER_WITNESS)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return registerAWitness(activeAccountKey, activeAccountName, websiteUrl)
    }

    /** unregister a witness
     * @param userActiveKey active key of [witness], who wants to unregister
     * @param witness name of witness to unregister
     *  * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun unRegisterWitness(
            userActiveKey: String,
            witness: CyberName
    ): Either<TransactionSuccessful<WitnessVoteResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val witnessRequest = UnRegWitnessRequestAbi(witness)

            val operationHex = squisher.squishUnRegWitnessRequestAbi(witnessRequest).toHex()

            pushTransaction<WitnessVoteResult>(
                    CyberContracts.CTRL,
                    CyberActions.UNREGISTER_WITNESS,
                    MyTransactionAuthorizationAbi(witness.name),
                    operationHex,
                    userActiveKey
            )

        }
        return callTilTimeoutExceptionVanishes(callable)
    }


    /** unregister a witness
     * This method assumes that you have added account with keys to [keyStorage]
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * @throws IllegalStateException if active account not set
     */

    fun unRegisterWitness(): Either<TransactionSuccessful<WitnessVoteResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.CTRL, CyberActions.UNREGISTER_WITNESS)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return unRegisterWitness(activeAccountKey, activeAccountName)
    }

    /** starts balloting of a witness
     * @param userActiveKey active key of [witness], who wants to start
     * @param witness name of witness to start
     *  * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun startWitness(
            witness: CyberName,
            userActiveKey: String
    ): Either<TransactionSuccessful<WitnessNameResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val witnessRequest = WitnessNameAbi(witness)

            val operationHex = squisher.squishWitnessNameAbi(witnessRequest).toHex()

            pushTransaction<WitnessNameResult>(
                    CyberContracts.CTRL,
                    CyberActions.START_WITNESS,
                    MyTransactionAuthorizationAbi(witness.name),
                    operationHex,
                    userActiveKey
            )

        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    /** starts balloting of a witness. Method uses active account from [keyStorage] as witness name.
     * Method assumes, that active account  is set
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun startWitness(): Either<TransactionSuccessful<WitnessNameResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.CTRL, CyberActions.START_WITNESS)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return startWitness(activeAccountName, activeAccountKey)
    }

    /** stops balloting of a witness. You cannot vote for a stopped witness, but unvoting is availible.
     * If there is not votes for a witness you can delete it.
     * @param userActiveKey active key of [witness], who wants to stop
     * @param witness name of witness to stop
     *  * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun stopWitness(
            witness: CyberName,
            userActiveKey: String
    ): Either<TransactionSuccessful<WitnessNameResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val witnessRequest = WitnessNameAbi(witness)

            val operationHex = squisher.squishWitnessNameAbi(witnessRequest).toHex()

            pushTransaction<WitnessNameResult>(
                    CyberContracts.CTRL,
                    CyberActions.STOP_WITNESS,
                    MyTransactionAuthorizationAbi(witness.name),
                    operationHex,
                    userActiveKey
            )

        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    /** stops balloting of a witness. You cannot vote for a stopped witness, but unvoting is availible.
     * If there is not votes for a witness you can delete it.
     * This method assumes that you have added account with keys to [keyStorage]
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun stopWitness(): Either<TransactionSuccessful<WitnessNameResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.CTRL, CyberActions.STOP_WITNESS)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return stopWitness(activeAccountName, activeAccountKey)
    }

    /**vote for post or comment, using credentials of active account from [keyStorage]
     * @param postOrCommentAuthor author of post or comment
     * @param postOrCommentPermlink permlink of post or comment
     * @param voteStrength voting strength. Might be [-10_000..10_000]. Set 0 to unvote
     *  @throws IllegalStateException if active account not set
     *   @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun vote(
            postOrCommentAuthor: CyberName,
            postOrCommentPermlink: String,
            voteStrength: Short
    ): Either<TransactionSuccessful<VoteResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.PUBLICATION,
                when {
                    voteStrength == 0.toShort() -> CyberActions.UN_VOTE
                    voteStrength > 0.toShort() -> CyberActions.UP_VOTE
                    else -> CyberActions.DOWN_VOTE
                }

        )
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return vote(
                activeAccountName, activeAccountKey, postOrCommentAuthor, postOrCommentPermlink, voteStrength
        )
    }

    /**vote for post or comment
     * @param fromAccount account name of voter
     * @param userActiveKey active key of [fromAccount]
     * @param postOrCommentAuthor author of post or comment
     * @param voteStrength voting strength. Might be [-10_000..10_000]. Set 0 to unvote
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun vote(
            fromAccount: CyberName,
            userActiveKey: String,
            postOrCommentAuthor: CyberName,
            postOrCommentPermlink: String,
            voteStrength: Short
    ): Either<TransactionSuccessful<VoteResult>, GolosEosError> {
        val callable = Callable {
            val squisher = createBinaryConverter()

            val discussionId =
                    DiscussionIdAbi(postOrCommentAuthor, postOrCommentPermlink)

            val operationHex = if (voteStrength == 0.toShort()) squisher
                    .squishUnVoteRequestAbi(UnVoteRequestAbi(fromAccount, discussionId)).toHex()
            else squisher.squishVoteRequestAbi(
                    VoteRequestAbi(
                            fromAccount, discussionId,
                            Math.abs(voteStrength.toInt()).toShort()
                    )
            ).toHex()

            pushTransaction<VoteResult>(
                    CyberContracts.PUBLICATION,
                    if (voteStrength == 0.toShort()) CyberActions.UN_VOTE else if (voteStrength > 0) CyberActions.UP_VOTE else CyberActions.DOWN_VOTE,
                    MyTransactionAuthorizationAbi(fromAccount.name),
                    operationHex,
                    userActiveKey
            )

        }
        return callTilTimeoutExceptionVanishes(callable)

    }

    /** method for account creation.
     * currently consists of 5 steps:
     * 1. create eos account
     * 2. open vesting balance [openVestingBalance]
     * 3. open token balance [openTokenBalance]
     * 4. issuing tokens to new user [issueTokens]
     * 5. transferring some GLS to "gls.vesting" user [transfer]
     * if one of this steps fails - you need do it manually, to fully init new user.
     * @param newAccountName account name of new account. must be [CyberName] compatible. Format - "[a-z0-5.]{0,12}"
     * @param newAccountMasterPassword master password for generating keys for newly created account.
     * method uses [AuthUtils.generatePrivateWiFs] for generating private key - so can you. Also
     * [AuthUtils.generatePublicWiFs] for acquiring public keys
     * @param cyberCreatePermissionKey key of "gls" for "newaccount" action with "active" permission
     * @throws IllegalStateException if method failed to open vesting or token balance, issue tokens or transfer it to "gls.vesting
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun createAccount(
            newAccountName: String,
            newAccountMasterPassword: String,
            cyberCreatePermissionKey: String
    ): Either<TransactionSuccessful<AccountCreationResult>, GolosEosError> {
        CyberName(newAccountName)
        val creatorAccountName = CyberContracts.GLS.toString()

        val keys = AuthUtils.generatePublicWiFs(newAccountName, newAccountMasterPassword, AuthType.values())

        val callable = Callable {
            val writer = AbiBinaryGenTransactionWriter(CompressionType.NONE)

            val newAccArgs = NewAccountArgs(
                    CyberContracts.GLS.toString(),
                    newAccountName,
                    AccountRequiredAuthAbi(
                            1,
                            listOf(AccountKeyAbi(keys[AuthType.OWNER]!!.replaceFirst("GLS", "EOS"), 1)),
                            emptyList(), emptyList()
                    ),
                    AccountRequiredAuthAbi(
                            1,
                            listOf(AccountKeyAbi(keys[AuthType.ACTIVE]!!.replaceFirst("GLS", "EOS"), 1)),
                            emptyList(),
                            emptyList()
                    )
            )
            val newAccBody = NewAccountBody(newAccArgs)
            val hex = writer.squishNewAccountBody(newAccBody).toHex()
            pushTransaction<AccountCreationResult>(
                    CyberContracts.CYBER,
                    CyberActions.NEW_ACCOUNT, MyTransactionAuthorizationAbi(creatorAccountName,
                    "createuser"),
                    hex,
                    cyberCreatePermissionKey
            )
        }
        val createAnswer = callTilTimeoutExceptionVanishes(callable)

        if (createAnswer is Either.Failure) return createAnswer

        val openVestingResult = openVestingBalance(newAccountName.toCyberName(), cyberCreatePermissionKey)

        if (openVestingResult is Either.Failure) throw IllegalStateException(
                "error initializing of account $newAccountName" +
                        "during openVestingBalance()"
        )

        val openTokenResult = openTokenBalance(newAccountName.toCyberName(), cyberCreatePermissionKey)

        if (openTokenResult is Either.Failure) throw IllegalStateException(
                "error initializing of account $newAccountName" +
                        "during openTokenBalance()"
        )

        val issueResult = issueTokens(newAccountName.toCyberName(), cyberCreatePermissionKey, "3.000 GOLOS")

        if (issueResult is Either.Failure) throw IllegalStateException(
                "error initializing of account $newAccountName\n" +
                        "during issueTokens()"
        )

        val activeKey = AuthUtils.generatePrivateWiFs(
                newAccountName,
                newAccountMasterPassword,
                arrayOf(AuthType.ACTIVE)
        )[AuthType.ACTIVE]!!

        val transferResult = transfer(
                activeKey,
                newAccountName.toCyberName(),
                CyberContracts.VESTING.toString().toCyberName(),
                "1.000", "GOLOS"
        )

        if (transferResult is Either.Failure) throw IllegalStateException(
                "error initializing of account $newAccountName" +
                        "during transfer() to ${CyberContracts.VESTING}"
        )

        return createAnswer
    }

    /** method for opening vesting balance of account. used in [createAccount] as one of the steps of
     * new account creation
     * @param forUser account name
     * @param cyberKey key of "cyber" with "createuser" permission
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */

    fun openVestingBalance(
            forUser: CyberName,
            cyberKey: String
    ) = openBalance(forUser, UserBalance.VESTING, cyberKey)

    /** method for opening token balance of account. used in [createAccount] as one of the steps of
     * new account creation
     * @param forUser account name
     * @param cyberCreatePermissionKey key of "cyber" with "createuser" permission
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */

    fun openTokenBalance(
            forUser: CyberName,
            cyberCreatePermissionKey: String
    ) =
            openBalance(forUser, UserBalance.TOKEN, cyberCreatePermissionKey)

    enum class UserBalance { VESTING, TOKEN }

    private fun openBalance(
            newAccountName: CyberName,
            type: UserBalance,
            cyberCreatePermissionKey: String
    ): Either<TransactionSuccessful<VestingReponse>, GolosEosError> {
        val creatorAccountName = CyberContracts.GLS.toString()

        val createVestingCallable = Callable {
            val writer = createBinaryConverter()
            val request = VestingStartRequestAbi(
                    newAccountName, CyberName(creatorAccountName),
                    when (type) {
                        UserBalance.TOKEN -> 3
                        UserBalance.VESTING -> 6
                    }
            )

            val result = writer.squishVestingStartRequestAbi(request)

            val hex = result.toHex()

            pushTransaction<VestingReponse>(
                    when (type) {
                        UserBalance.TOKEN -> CyberContracts.CYBER_TOKEN
                        UserBalance.VESTING -> CyberContracts.VESTING
                    },
                    CyberActions.OPEN_VESTING,
                    MyTransactionAuthorizationAbi(creatorAccountName, "createuser"),
                    hex,
                    cyberCreatePermissionKey
            )
        }

        return callTilTimeoutExceptionVanishes(createVestingCallable)
    }


    /** method for issuing tokens for [forUser] recipient. Also, used as part of new account creation in [createAccount]
     * @param forUser account name
     * @param issuerKey key of "gls" with "issue" permission
     * @param amount amount of tokens to issue.  Must have 3 points precision, like 12.000 or 0.001
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun issueTokens(
            forUser: CyberName,
            issuerKey: String,
            amount: String,
            memo: String = ""
    ): Either<TransactionSuccessful<Any>, GolosEosError> {

        val issuerTokenCallable = Callable {

            val actionAbis = ArrayList<MyActionAbi>()

            val writer = createBinaryConverter()
            val issueRequest = IssueRequestAbi(CyberContracts.GLS.toString().toCyberName(), amount, memo)
            val result = writer.squishIssueRequestAbi(issueRequest)

            if (config.logLevel == LogLevel.BODY) config.httpLogger
                    ?.log("issue request  = ${moshi.adapter<IssueRequestAbi>(IssueRequestAbi::class.java).toJson(issueRequest)}")

            var hex = result.toHex()
            val issueAbi = MyActionAbi(
                    CyberContracts.CYBER_TOKEN.toString(), CyberActions.ISSUE.toString(),
                    listOf(MyTransactionAuthorizationAbi(CyberContracts.GLS.toString(), "issue")), hex
            )
            if (config.logLevel == LogLevel.BODY) config.httpLogger
                    ?.log("issue transaction = ${moshi.adapter<MyActionAbi>(MyActionAbi::class.java).toJson(issueAbi)}")

            actionAbis.add(issueAbi)


            hex = createBinaryConverter().squishMyTransferArgsAbi(
                    MyTransferArgsAbi(CyberContracts.GLS.toString(), forUser.name, amount, memo)
            ).toHex()

            actionAbis.add(
                    MyActionAbi(
                            CyberContracts.CYBER_TOKEN.toString(), CyberActions.TRANSFER.toString(),
                            listOf(MyTransactionAuthorizationAbi(CyberContracts.GLS.toString(), "issue")), hex
                    )
            )


            transactionPusher.pushTransaction(actionAbis, EosPrivateKey(issuerKey), Any::class.java)

        }

        return callTilTimeoutExceptionVanishes(issuerTokenCallable)
    }

    /**method sets  nickname for active user. Method assumes, that there is some active user in [keyStorage]
     * Nick must match [0-9a-z.-]{1,32}
     * @param owner owner of domain
     * @param newUserName new nickName for user
     * @param creatorKey active key of [owner]
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun newUserName(owner: CyberName,
                    newUserName: String,
                    creatorKey: String): Either<TransactionSuccessful<CreateUserNameResult>, GolosEosError> {
        return newUserName(owner, resolveKeysFor(CyberContracts.DOMAIN, CyberActions.SET_NEW_USER_NAME).first,
                newUserName, creatorKey)
    }

    /**method sets  nickname [forUser].
     * Nick must match [0-9a-z.-]{1,32}
     * @param owner owner of domain
     * @param newUserName new nickName for user
     * @param creatorKey active key of [owner]
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun newUserName(owner: CyberName,
                    forUser: CyberName,
                    newUserName: String,
                    creatorKey: String): Either<TransactionSuccessful<CreateUserNameResult>, GolosEosError> {
        if (!newUserName.matches("[0-9a-z.-]{1,32}".toRegex())) throw java.lang.IllegalArgumentException("nick must match [0-9a-z.-]{1,32}")
        val setUserNameCallable = Callable {
            val result = createBinaryConverter()
                    .squishCreateUserNameAbi(CreateUserNameAbi(owner,
                            forUser,
                            newUserName))
                    .toHex()
            pushTransaction<CreateUserNameResult>(CyberContracts.DOMAIN,
                    CyberActions.SET_NEW_USER_NAME,
                    MyTransactionAuthorizationAbi(owner.name, "createuser"), result, creatorKey)
        }
        return callTilTimeoutExceptionVanishes(setUserNameCallable)
    }

    /** method for fetching posts of certain community from cyberway microservices.
     * return objects may differ, depending on auth state of current user.
     * @param communityId id of community
     * @param type type of parsing to apply to content. According to [type] returning [DiscussionsResult]'s [DiscussionContent] may vary:
     * for [ContentParsingType.MOBILE] there would rows of text and images, for [ContentParsingType.WEB] there would be 'body' with web parsing rules to apply,
     * for [ContentParsingType.RAW] there would be 'raw' field, with contents as is
     * @param limit limit of returned discussions
     * @param sort [DiscussionTimeSort.INVERTED] if you need new posts first, [DiscussionTimeSort.SEQUENTIALLY] if you need old first
     * @param sequenceKey paging key for querying next page of discussions. is from [DiscussionsResult.getSequenceKey].
     * null, if you want posts from beginning
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * also this exception may occur during authorization in case of active user change in [keyStorage], if there is some query in process.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun getCommunityPosts(
            communityId: String,
            type: ContentParsingType,
            limit: Int,
            sort: DiscussionTimeSort,
            sequenceKey: String? = null
    ) = apiService.getDiscussions(PostsFeedType.COMMUNITY, sort, type, sequenceKey, limit, null, communityId)


    /** method for fetching user subscribed communities posts
     * return objects may differ, depending on auth state of current user.
     * @param user user, which subscriptions to fetch
     * @param type type of parsing to apply to content. According to [type] returning [DiscussionsResult]'s [DiscussionContent] may vary:
     * for [ContentParsingType.MOBILE] there would rows of text and images, for [ContentParsingType.WEB] there would be 'body' with web parsing rules to apply,
     * for [ContentParsingType.RAW] there would be 'raw' field, with contents as is
     * @param limit limit of returned discussions
     * @param sort [DiscussionTimeSort.INVERTED] if you need new posts first, [DiscussionTimeSort.SEQUENTIALLY] if you need old first
     * @param sequenceKey paging key for querying next page of discussions. is from [DiscussionsResult.getSequenceKey]
     * null, if you want posts from beginning
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * also this exception may occur during authorization in case of active user change in [keyStorage], if there is some query in process.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun getUserSubscriptions(
            user: CyberName,
            type: ContentParsingType,
            limit: Int,
            sort: DiscussionTimeSort,
            sequenceKey: String?
    ) = apiService.getDiscussions(
            PostsFeedType.SUBSCRIPTIONS,
            sort, type, sequenceKey, limit, user.name, null
    )

    /** method for fetching posts of certain user
     * return objects may differ, depending on auth state of current user.
     *  in [CyberDiscussion] returned by this method, in [ContentBody] [ContentBody.preview] is not empty
     * @param user user, which subscriptions to fetch
     * @param type type of parsing to apply to content. According to [type] returning [DiscussionsResult]'s [DiscussionContent] may vary:
     * for [ContentParsingType.MOBILE] there would rows of text and images, for [ContentParsingType.WEB] there would be 'body' with web parsing rules to apply,
     * for [ContentParsingType.RAW] there would be 'raw' field, with contents as is
     * @param limit limit of returned discussions
     * @param sort [DiscussionTimeSort.INVERTED] if you need new posts first, [DiscussionTimeSort.SEQUENTIALLY] if you need old first
     * @param sequenceKey paging key for querying next page of discussions. is from [DiscussionsResult.getSequenceKey]
     * null, if you want posts from beginning
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * also this exception may occur during authorization in case of active user change in [keyStorage], if there is some query in process.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun getUserPosts(
            user: CyberName,
            type: ContentParsingType,
            limit: Int,
            sort: DiscussionTimeSort,
            sequenceKey: String? = null
    ) =
            apiService.getDiscussions(
                    PostsFeedType.USER_POSTS,
                    sort,
                    type,
                    sequenceKey,
                    limit,
                    user.name,
                    null
            )


    /** method for fetching particular post
     * return objects may differ, depending on auth state of current user.
     * in [CyberDiscussion] returned by this method, in [ContentBody] [ContentBody.full] is not empty
     * @param user user, which post to fetch
     * @param permlink permlink of post to fetch
     * @param parsingType type of parsing to apply to content. According to [parsingType] returning [DiscussionsResult]'s [DiscussionContent] may vary:
     * for [ContentParsingType.MOBILE] there would rows of text and images, for [ContentParsingType.WEB] there would be 'body' with web parsing rules to apply,
     * for [ContentParsingType.RAW] there would be 'raw' field, with contents as is
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * also this exception may occur during authorization in case of active user change in [keyStorage], if there is some query in process.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */


    fun getPost(
            user: CyberName,
            permlink: String,
            parsingType: ContentParsingType
    ) = apiService.getPost(user.name, permlink, parsingType)


    /** method for fetching particular comment
     * return objects may differ, depending on auth state of current user.
     * @param user user, which comment to fetch
     * @param permlink permlink of comment to fetch
     * @param parsingType type of parsing to apply to content. According to [parsingType] returning [DiscussionsResult]'s [DiscussionContent] may vary:
     * for [ContentParsingType.MOBILE] there would rows of text and images, for [ContentParsingType.WEB] there would be 'body' with web parsing rules to apply,
     * for [ContentParsingType.RAW] there would be 'raw' field, with contents as is
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * also this exception may occur during authorization in case of active user change in [keyStorage], if there is some query in process.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun getComment(
            user: CyberName,
            permlink: String,
            parsingType: ContentParsingType
    ) = apiService.getComment(user.name, permlink, parsingType)

    /** method for fetching comments particular post
     * return objects may differ, depending on auth state of current user.
     * @param user user of original post
     * @param permlink permlink of original post
     * @param parsingType type of parsing to apply to content. According to [parsingType] returning [DiscussionsResult]'s [DiscussionContent] may vary:
     * for [ContentParsingType.MOBILE] there would rows of text and images, for [ContentParsingType.WEB] there would be 'body' with web parsing rules to apply,
     * for [ContentParsingType.RAW] there would be 'raw' field, with contents as is
     * @param limit number of comments to fetch. Comments are fetched sequentially, without concerning on comment level
     * @param sort [DiscussionTimeSort.INVERTED] if you need new comments first, [DiscussionTimeSort.SEQUENTIALLY] if you need old first
     * @param sequenceKey paging key for querying next page of comments. is from [DiscussionsResult.getSequenceKey]
     * null, if you want posts from beginning
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * also this exception may occur during authorization in case of active user change in [keyStorage], if there is some query in process.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun getCommentsOfPost(
            user: CyberName,
            permlink: String,
            parsingType: ContentParsingType,
            limit: Int,
            sort: DiscussionTimeSort,
            sequenceKey: String? = null
    ) =

            apiService.getComments(
                    sort, sequenceKey, limit,
                    CommentsOrigin.COMMENTS_OF_POST, parsingType,
                    user.name, permlink)

    /** method for fetching replies to particular user
     * return objects may differ, depending on auth state of current user.
     * @param user user which replies to fetch
     * @param parsingType type of parsing to apply to content. According to [parsingType] returning [DiscussionsResult]'s [DiscussionContent] may vary:
     * for [ContentParsingType.MOBILE] there would rows of text and images, for [ContentParsingType.WEB] there would be 'body' with web parsing rules to apply,
     * for [ContentParsingType.RAW] there would be 'raw' field, with contents as is
     * @param limit number of comments to fetch. Comments are fetched sequentially, without concerning on comment level
     * @param sort [DiscussionTimeSort.INVERTED] if you need new comments first, [DiscussionTimeSort.SEQUENTIALLY] if you need old first
     * @param sequenceKey paging key for querying next page of comments. is from [DiscussionsResult.getSequenceKey]
     * null, if you want posts from beginning
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * also this exception may occur during authorization in case of active user change in [keyStorage], if there is some query in process.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun getUserReplies(
            user: CyberName,
            parsingType: ContentParsingType,
            limit: Int,
            sort: DiscussionTimeSort,
            sequenceKey: String? = null
    ) =
            apiService.getComments(
                    sort, sequenceKey, limit,
                    CommentsOrigin.REPLIES, parsingType,
                    user.name, null)

    /** method for fetching comments particular user
     * return objects may differ, depending on auth state of current user.
     * @param user name of user, which comments we need
     * @param parsingType type of parsing to apply to content. According to [parsingType] returning [DiscussionsResult]'s [DiscussionContent] may vary:
     * for [ContentParsingType.MOBILE] there would rows of text and images, for [ContentParsingType.WEB] there would be 'body' with web parsing rules to apply,
     * for [ContentParsingType.RAW] there would be 'raw' field, with contents as is
     * @param limit number of comments to fetch.
     * @param sort [DiscussionTimeSort.INVERTED] if you need new comments first, [DiscussionTimeSort.SEQUENTIALLY] if you need old first
     * @param sequenceKey paging key for querying next page of comments. is from [DiscussionsResult.getSequenceKey]
     * null, if you want posts from beginning
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * also this exception may occur during authorization in case of active user change in [keyStorage], if there is some query in process.
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */

    fun getCommentsOfUser(
            user: CyberName,
            parsingType: ContentParsingType,
            limit: Int,
            sort: DiscussionTimeSort,
            sequenceKey: String? = null
    ): Either<DiscussionsResult, ApiResponseError> =
            apiService.getComments(
                    sort, sequenceKey, limit,
                    CommentsOrigin.COMMENTS_OF_USER, parsingType,
                    user.name, null)

    /**Do not use, would be changed soon*/
    fun getSubscriptionsToUsers(ofUser: CyberName, limit: Int, sequenceKey: String?) = apiService.getSubscriptions(ofUser, limit, SubscriptionType.USER, sequenceKey)

    /**Do not use, would be changed soon*/
    fun getSubscriptionsToCommunities(ofUser: CyberName, limit: Int, sequenceKey: String?) = apiService.getSubscriptions(ofUser, limit, SubscriptionType.COMMUNITY, sequenceKey)

    /**Do not use, would be changed soon*/
    fun getUsersSubscribedToUser(user: CyberName, limit: Int, sequenceKey: String?) = apiService.getSubscribers(user, limit, SubscriptionType.USER, sequenceKey)

    /**Do not use, would be changed soon*/
    fun getCommunitiesSubscribedToUser(ofUser: CyberName, limit: Int, sequenceKey: String?) = apiService.getSubscribers(ofUser, limit, SubscriptionType.COMMUNITY, sequenceKey)


    /** method for fetching  metedata of some user
     * @param user name of user, which metadata is  fetched
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun getUserMetadata(user: CyberName): Either<UserMetadataResult, ApiResponseError> =
            apiService.getUserMetadata(user.name)


    /** method will block thread until [blockNum] would consumed by prism services
     * @param blockNum num of block to wait
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun waitForABlock(blockNum: Long): Either<ResultOk, ApiResponseError> = apiService.waitBlock(blockNum)

    /** method will block thread until [transactionId] would be consumed by prism services. Old transaction are not stored in services.
     * @param transactionId id of transaction to wait
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun waitForTransaction(transactionId: String): Either<ResultOk, ApiResponseError> = apiService.waitForTransaction(transactionId)


    /**get processed embed link for some raw "https://site.com/content" using iframely service
     * @param forLink raw link of site content
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun getEmbedIframely(forLink: String): Either<IFramelyEmbedResult, ApiResponseError> =
            apiService.getIframelyEmbed(forLink)

    /**get processed embed link for some raw "https://site.com/content" using oembed service
     * @param forLink raw link of site content
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun getEmbedOembed(forLink: String): Either<OEmbedResult, ApiResponseError> = apiService.getOEmdedEmbed(forLink)


    /** method subscribes mobile device for push notifications in FCM.
     * method requires authorization
     * @param deviceId id of device or installation.
     * @param fcmToken token of app installation in FCM.
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun subscribeOnMobilePushNotifications(deviceId: String, fcmToken: String): Either<ResultOk, ApiResponseError> = apiService.subscribeOnMobilePushNotifications(deviceId, fcmToken)

    /** method unSubscribes mobile device from push notifications in FCM.
     *  method requires authorization
     * @param deviceId id of device or installation.
     * @param fcmToken token of app installation in FCM.
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun unSubscribeOnNotifications(deviceId: String, fcmToken: String): Either<ResultOk, ApiResponseError> = apiService.unSubscribeOnNotifications(deviceId, fcmToken)

    /**method for setting various settings for user. If any of setting param is null, this settings will not change.
     * All setting are individual for every [deviceId]
     * method requires authorization
     * @param deviceId id of device or installation.
     * @param newBasicSettings schema-free settings, used for saving app personalization.
     * @param newWebNotifySettings settings of online web notifications.
     * @param newMobilePushSettings settings of mobile push notifications. Uses FCM.
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun setUserSettings(deviceId: String, newBasicSettings: Any?,
                        newWebNotifySettings: WebShowSettings?,
                        newMobilePushSettings: MobileShowSettings?): Either<ResultOk, ApiResponseError> = apiService.setNotificationSettings(deviceId, newBasicSettings, newWebNotifySettings, newMobilePushSettings)

    /**method for retreiving user setting. Personal for evert [deviceId]
     * method requires authorization
     * @param deviceId id of device or installation.
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun getUserSettings(deviceId: String): Either<UserSettings, ApiResponseError> = apiService.getNotificationSettings(deviceId)

    /**method for retreiving history of notifications.
     * method requires authorization
     * @param userProfile name of user which notifications to retreive.
     * @param afterId id of next page of events. Set null if you want first page.
     * @param limit number of event to retreive
     * @param markAsViewed set true, if you want to set all retreived notifications as viewed
     * @param freshOnly set true, if you want get only fresh notifcaitons
     * @param types list of types of notifcaitons you want to get
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun getEvents(userProfile: String, afterId: String?, limit: Int?, markAsViewed: Boolean?,
                  freshOnly: Boolean?, types: List<EventType>): Either<EventsData, ApiResponseError> = apiService.getEvents(userProfile, afterId, limit, markAsViewed, freshOnly, types)

    /**mark certain events as unfresh, eg returning 'fresh' property as false
     * method requires authorization
     * @param ids list of id's to set as read
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun markEventsAsNotFresh(ids: List<String>): Either<ResultOk, ApiResponseError> = apiService.markEventsAsRead(ids)

    /**mark certain all events history of authorized user as not fresh
     * method requires authorization
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun markAllEventsAsNotFresh(): Either<ResultOk, ApiResponseError> = apiService.markAllEventsAsRead()

    /**method for retreving count of fresh events of authorized user.
     * method requires authorization
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun getFreshNotificationCount(profileId: String): Either<FreshResult, ApiResponseError> = apiService.getUnreadCount(profileId)

    /**method returns current state of user registration process, user gets identified by [user] or
     * by [phone]
     *  @param user name of user, which registration state get fetched.
     *  @param phone  of user, which registration state get fetched.
     *  @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun getRegistrationState(
            user: String?,
            phone: String?
    ): Either<UserRegistrationStateResult, ApiResponseError> =
            apiService.getRegistrationStateOf(userId = user, phone = phone)


    /** method leads to sending sms code to user's [phone]. proper [testingPass] makes backend to omit this check
     *  @param captcha capthc string
     *  @param phone  of user for sending sms verification code
     *  @param testingPass pass to omit cpatcha and phone checks
     *  @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */

    fun firstUserRegistrationStep(captcha: String?, phone: String, testingPass: String?) =
            apiService.firstUserRegistrationStep(captcha, phone, testingPass)

    /** method used to verify [phone] by sent [code] through sms. Second step of registration
     *  @param code sms code sent to [phone]
     *  @param phone  of user
     *  @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun verifyPhoneForUserRegistration(phone: String, code: Int) =
            apiService.verifyPhoneForUserRegistration(phone, code)

    /** method used to connect verified [user] name with [phone]. Third step of registration
     *  @param user name to associate with [phone]
     *  @param phone verified phone
     *  @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun setVerifiedUserName(user: String, phone: String) = apiService.setVerifiedUserName(user, phone)

    /** method used to finalize registration of user in cyberway blockchain. Final step of registration
     *  @param userName name of user
     *  @param owner public owner key of user
     *  @param active public active key of user
     *  @param posting public posting key of user
     *  @param memo public memo key of user
     *  @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun writeUserToBlockChain(
            userName: String,
            owner: String,
            active: String,
            posting: String,
            memo: String
    ) = apiService.writeUserToBlockchain(userName, owner, active, posting, memo)


    /** method used to resend sms code to user during phone verification
     *  @param forUser name of user
     *  @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun resendSmsCode(forUser: String, @Suppress("UNUSED_PARAMETER") unused: Int = 0) = apiService.resendSmsCode(forUser, null)

    /** method used to resend sms code to user during phone verification
     *  @param phone phone of user to verify
     *  @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun resendSmsCode(phone: String) = apiService.resendSmsCode(null, phone)


    /**part of auth process. It consists of 3 steps:
     * 1. getting secret string using method [getAuthSecret]
     * 2. signing it with [StringSigner]
     * 3. sending result using [authWithSecret] method
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun getAuthSecret(): Either<AuthSecret, ApiResponseError> = apiService.getAuthSecret()

    /**part of auth process. It consists of 3 steps:
     * 1. getting secret string using method [getAuthSecret]
     * 2. signing it with [StringSigner]
     * 3. sending result using [authWithSecret] method
     *  @param user userid, userName, domain name, or whateve current version of services willing to accept
     *  @param secret secret string, obtained from [getAuthSecret] method
     *  @param signedSecret [secret] signed with [StringSigner]
     *  @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */

    fun authWithSecret(user: String,
                       secret: String,
                       signedSecret: String): Either<AuthResult, ApiResponseError> = apiService.authWithSecret(user, secret, signedSecret)

    /**disconnects from microservices, effectively unaithing
     * Method will result  throwing all pending socket requests.
     * */
    fun unAuth() = apiService.unAuth()

    fun isUserAuthed(): Either<Boolean, java.lang.Exception> {
        return try {
            val resp = markEventsAsNotFresh(emptyList())
            Either.Success(resp is Either.Success)
        } catch (e: java.lang.Exception) {
            Either.Failure(e)
        }
    }


    /** method for fetching  account of some user
     * @param user name of user, which account is  fetched
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     */
    fun getUserAccount(user: CyberName): Either<UserProfile, GolosEosError> {
        return try {
            val accResponse = chainApi.getAccount(AccountName(user.name)).blockingGet()
            if (accResponse.isSuccessful) {
                val acc = accResponse.body()!!
                return Either.Success(
                        UserProfile(acc.account_name,
                                acc.head_block_num,
                                acc.head_block_time,
                                acc.privileged,
                                acc.last_code_update,
                                acc.created,
                                acc.core_liquid_balance,
                                acc.ram_quota,
                                acc.net_weight,
                                acc.cpu_weight,
                                acc.ram_usage,
                                acc.permissions.map { accountPermission ->
                                    UserProfile.AccountPermission(accountPermission.perm_name, accountPermission.parent,
                                            UserProfile.AccountRequiredAuth(
                                                    accountPermission.required_auth.threshold,
                                                    accountPermission.required_auth.keys.map { accountKey ->
                                                        UserProfile.AccountKey(accountKey.key, accountKey.weight)
                                                    },
                                                    accountPermission.required_auth.accounts.map { accountAuth ->
                                                        UserProfile.AccountAuth(accountAuth.permission.run {
                                                            UserProfile.AccountAuthPermission(actor, permission)
                                                        }, accountAuth.weight)
                                                    }
                                            ))
                                })
                )
            } else {
                return Either.Failure(
                        moshi.adapter<io.golos.cyber4j.model.GolosEosError>(io.golos.cyber4j.model.GolosEosError::class.java).fromJson(
                                accResponse.errorBody()!!.string()
                        )!!
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Either.Failure(
                    GolosEosError(0, e.message ?: e.localizedMessage
                    ?: "", GolosEosError.Error(0, "", "", emptyArray()))
            )

        }
    }

    /** method for uploading images.
     * @param file file with image
     * @return [io.golos.cyber4j.utils.Either.Success] if transaction succeeded, otherwise [io.golos.cyber4j.utils.Either.Failure]
     * */
    fun uploadImage(file: File): Either<String, GolosEosError> {
        return try {
            Either.Success(chainApi.uploadImage(file).blockingGet())
        } catch (e: java.lang.Exception) {
            Either.Failure(
                    GolosEosError(0, e.message ?: e.localizedMessage
                    ?: "", GolosEosError.Error(0, "", "", emptyArray()))
            )
        }

    }

    /** transfer [amount] of [currency] [from] user [to] user
     * @param key active key [from] user
     * @param from user from wallet you want to transfer
     * @param to recipient of money
     * @param amount amount of [currency] to transfer. Must have 3 points precision, like 12.000 or 0.001
     * @param currency currency to transfer. GLS, for example
     * @param memo some additional info, that added to transfer
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     */
    fun transfer(
            key: String,
            from: CyberName,
            to: CyberName,
            amount: String,
            currency: String,
            memo: String = ""
    ): Either<TransactionSuccessful<TransferResult>, GolosEosError> {

        if (!amount.matches("([0-9]+\\.[0-9]{3})".toRegex())) throw IllegalArgumentException("wrong currency format. Must have 3 points precision, like 12.000 or 0.001")

        val callable = Callable {
            val hex = createBinaryConverter().squishMyTransferArgsAbi(
                    MyTransferArgsAbi(
                            from.name,
                            to.name,
                            "$amount $currency",
                            memo
                    )
            ).toHex()
            pushTransaction<TransferResult>(
                    CyberContracts.TOKEN, CyberActions.TRANSFER,
                    MyTransactionAuthorizationAbi(from), hex, key
            )
        }

        return callTilTimeoutExceptionVanishes(callable)
    }

    /** transfer [amount] of [currency] [to] user, from active account in [keyStorage]
     * @param to recipient of money
     * @param amount amount of [currency] to transfer. Must have 3 points precision, like 12.000 or 0.001
     * @param currency currency to transfer. GLS, for example
     * @param memo some additional info, that added to transfer
     * @throws SocketTimeoutException if socket was unable to answer in [Cyber4JConfig.readTimeoutInSeconds] seconds
     */
    fun transfer(
            to: CyberName,
            amount: String,
            currency: String,
            memo: String = ""
    ): Either<TransactionSuccessful<TransferResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.TOKEN, CyberActions.TRANSFER)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second

        return transfer(
                activeAccountKey,
                activeAccountName,
                to,
                amount,
                currency,
                memo
        )
    }

    /** pin user to active user from [keyStorage]
     * @param pinning user to pin
     * @throws IllegalStateException if active user not set.
     */
    fun pin(pinning: CyberName): Either<TransactionSuccessful<PinResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.SOCIAL, CyberActions.PIN)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return pin(activeAccountKey, activeAccountName, pinning)
    }

    /**  pin user to [pinner]
     * @param activeKey [pinner] active ky
     * @param pinner user that pins
     * @param pinning user to pin to [pinner]
     */
    fun pin(
            activeKey: String,
            pinner: CyberName,
            pinning: CyberName
    ): Either<TransactionSuccessful<PinResult>, GolosEosError> {

        val callable = Callable {
            val hex = createBinaryConverter().squishPinRequestAbi(
                    PinRequestAbi(
                            pinner,
                            pinning
                    )
            ).toHex()
            pushTransaction<PinResult>(
                    CyberContracts.SOCIAL,
                    CyberActions.PIN,
                    pinner.toTransactionAuthAbi(),
                    hex,
                    activeKey
            )
        }
        return callTilTimeoutExceptionVanishes(callable = callable)
    }

    /** unPin user from active user from [keyStorage]
     * @param pinning user to unpin
     * @throws IllegalStateException if active user not set.
     */
    fun unPin(pinning: CyberName): Either<TransactionSuccessful<PinResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.SOCIAL, CyberActions.UN_PIN)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return unPin(activeAccountKey, activeAccountName, pinning)
    }

    /** unpin [pinning] from [pinner]
     * @param activeKey [pinner] active ky
     * @param pinner user that unpins
     * @param pinning user to unpin from [pinner]
     */

    fun unPin(
            activeKey: String,
            pinner: CyberName,
            pinning: CyberName
    ): Either<TransactionSuccessful<PinResult>, GolosEosError> {

        val callable = Callable {
            val hex = createBinaryConverter().squishPinRequestAbi(
                    PinRequestAbi(
                            pinner,
                            pinning
                    )
            ).toHex()
            pushTransaction<PinResult>(
                    CyberContracts.SOCIAL,
                    CyberActions.UN_PIN,
                    pinner.toTransactionAuthAbi(),
                    hex,
                    activeKey
            )
        }
        return callTilTimeoutExceptionVanishes(callable = callable)
    }

    /** block user for active user from [keyStorage]
     * @param user user to block
     * @throws IllegalStateException if active user not set.
     */
    fun block(user: CyberName): Either<TransactionSuccessful<BlockUserResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.SOCIAL, CyberActions.BLOCK)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return block(activeAccountKey, activeAccountName, user)
    }

    /** block [blocking] user for [blocker]
     * @param blockerActiveKey [blocker] active ky
     * @param blocker user that blocks [blocker]
     * @param blocking user to block
     */
    fun block(
            blockerActiveKey: String,
            blocker: CyberName,
            blocking: CyberName
    ): Either<TransactionSuccessful<BlockUserResult>, GolosEosError> {

        val callable = Callable {
            pushTransaction<BlockUserResult>(
                    CyberContracts.SOCIAL, CyberActions.BLOCK,
                    blocker.toTransactionAuthAbi(),
                    createBinaryConverter().squishBlockUserRequestAbi(
                            BlockUserRequestAbi(
                                    blocker,
                                    blocking
                            )
                    ).toHex(),
                    blockerActiveKey
            )
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    /** unBlock user for active user from [keyStorage]
     * @param user user to unblock
     * @throws IllegalStateException if active user not set.
     */
    fun unBlock(user: CyberName): Either<TransactionSuccessful<BlockUserResult>, GolosEosError> {
        val activeUser = resolveKeysFor(CyberContracts.SOCIAL, CyberActions.UN_BLOCK)
        val activeAccountName = activeUser.first
        val activeAccountKey = activeUser.second
        return unBlock(activeAccountKey, activeAccountName, user)
    }

    /** unBlock [blocking] user from [blocker]
     * @param blockerActiveKey [blocker] active ky
     * @param blocker user that unblocks [blocker]
     * @param blocking user that gets unblocked
     */
    fun unBlock(
            blockerActiveKey: String,
            blocker: CyberName,
            blocking: CyberName
    ): Either<TransactionSuccessful<BlockUserResult>, GolosEosError> {

        val callable = Callable {
            pushTransaction<BlockUserResult>(
                    CyberContracts.SOCIAL, CyberActions.UN_BLOCK,
                    blocker.toTransactionAuthAbi(),
                    createBinaryConverter().squishBlockUserRequestAbi(
                            BlockUserRequestAbi(
                                    blocker,
                                    blocking
                            )
                    ).toHex(),
                    blockerActiveKey
            )
        }
        return callTilTimeoutExceptionVanishes(callable)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun resolveKeysFor(contract: CyberContracts, action: CyberActions): kotlin.Pair<CyberName, String> {
        val activeAccountName = keyStorage.getActiveAccount()
        val activeAccountKey = keyStorage.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: throw IllegalStateException("you must set active key to account $activeAccountName")
        return activeAccountName to activeAccountKey
    }

    /**method closes all connections, pools, executors etc. After that instance is useless
     * */
    @ShutDownMethod
    fun shutdown() {
        synchronized(this) {
            chainApi.shutDown()
            apiService.shutDown()
        }
    }
}

private fun CyberName.toTransactionAuthAbi(): MyTransactionAuthorizationAbi = MyTransactionAuthorizationAbi(this.name)


/**[DefaultByteWriter] has a bug in utf-8 serialization, so we use custom one */
private fun createBinaryConverter(): AbiBinaryGenCyber4J {
    return AbiBinaryGenCyber4J(CyberwayByteWriter(), DefaultHexWriter(), CompressionType.NONE)
}