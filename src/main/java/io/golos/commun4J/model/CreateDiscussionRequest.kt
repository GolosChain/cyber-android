package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.*

//account,
//        permlink,
//        parentacc: '',
//        parentprmlnk: '',
//        beneficiaries: [],
//        tokenprop: 0,
//        vestpayment: true,
//        headermssg: title,
//        bodymssg: html,
//        languagemssg: 'ru',
//        tags: [{ tag: 'com.test' }],
//        jsonmetadata: '',
@Abi
internal class CreateDiscussionRequest(private val discussionId: DiscussionId,
                                       private val parentDiscussionId: DiscussionId,
                                       private val beneficiaries: List<Beneficiary>,
                                       private val headermssg: String,
                                       private val bodymssg: String,
                                       private val tags: List<Tag>,
                                       private val tokenprop: Long = 0,
                                       private val vestpayment: Boolean = true,
                                       private val language: String = "ru",
                                       private val jsonMetadata: String = "") {

    val getDiscussionId: DiscussionId
        @ChildCompress get() = discussionId

    val getParentDiscussionId: DiscussionId
        @ChildCompress get() = parentDiscussionId

    val getBeneficiaries: List<Beneficiary>
        @CollectionCompress get() = beneficiaries

    val getTokenProp: Long
        @LongCompress get() = tokenprop

    val getVestPayement: Byte
        @ByteCompress get() = (if (vestpayment) 1 else 0).toByte()

    val getHeadermssg: String
        @StringCompress get() = headermssg

    val getBodyMessage: String
        @StringCompress get() = bodymssg

    val getLanguage: String
        @StringCompress get() = language

    val getTags: List<Tag>
        @CollectionCompress get() = tags

    val getJsonmetadata: String
        @StringCompress get() = jsonMetadata

    override fun toString(): String {
        return "CreateDiscussionRequest(discussionId=$discussionId, parentDiscussionId=$parentDiscussionId, beneficiaries=$beneficiaries, headermssg='$headermssg', bodymssg='$bodymssg', tags=$tags, tokenprop=$tokenprop, vestpayment=$vestpayment, language='$language', jsonMetadata='$jsonMetadata')"
    }
}
