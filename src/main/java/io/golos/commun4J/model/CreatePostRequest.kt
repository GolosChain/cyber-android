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
internal class CreatePostRequest(private val account: CommunName,
                        private val permlink: String,
                        private val parentacc: CommunName,
                        private val parentprmlnk: String,
                        private val beneficiaries: List<Beneficiary>,
                        private val headermssg: String,
                        private val bodymssg: String,
                        private val tags: List<Tag>,
                        private val tokenprop: Long = 0,
                        private val vestpayment: Boolean = true,
                        private val language: String = "ru",
                        private val jsonMetadata: String = "") {

    val getAcc: String
        @NameCompress get() = account.name

    val getPermlink: String
        @StringCompress get() = permlink

    val getParentAcc: String
        @NameCompress get() = parentacc.name

    val getParentPermlink: String
        @StringCompress get() = parentprmlnk

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
        return "CreatePostRequest(account=$account, permlink='$permlink', parentacc=$parentacc, parentprmlnk='$parentprmlnk', beneficiaries=$beneficiaries, headermssg='$headermssg', bodymssg='$bodymssg', tags=$tags, tokenprop=$tokenprop, vestpayment=$vestpayment, language='$language', jsonMetadata='$jsonMetadata')"
    }

}
