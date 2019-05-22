package io.golos.cyber4j.model

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
internal class CreateDiscussionRequestAbi(private val discussionIdAbi: DiscussionIdAbi,
                                          private val parentDiscussionIdAbi: DiscussionIdAbi,
                                          private val beneficiaries: List<Beneficiary>,
                                          private val headermssg: String,
                                          private val bodymssg: String,
                                          private val tags: List<Tag>,
                                          private val tokenprop: Long = 0,
                                          private val vestpayment: Boolean = true,
                                          private val language: String = "ru",
                                          private val jsonMetadata: String = "",
                                          private val curatorPercentage: Short?) {

    val getDiscussionIdAbi: DiscussionIdAbi
        @ChildCompress get() = discussionIdAbi

    val getParentDiscussionIdAbi: DiscussionIdAbi
        @ChildCompress get() = parentDiscussionIdAbi

    val getBeneficiaries: List<Beneficiary>
        @CollectionCompress get() = beneficiaries

    val getTokenProp: Short
        @ShortCompress get() = tokenprop.toShort()

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

    val getCuratorPercentage: ByteArray
        @BytesCompress get() = curatorPercentage.asOptionalShortBytes()

    override

    fun toString(): String {
        return "CreateDiscussionRequestAbi(discussionIdAbi=$discussionIdAbi, parentDiscussionIdAbi=$parentDiscussionIdAbi, beneficiaries=$beneficiaries, headermssg='$headermssg', bodymssg='$bodymssg', tags=$tags, tokenprop=$tokenprop, vestpayment=$vestpayment, language='$language', jsonMetadata='$jsonMetadata')"
    }
}
