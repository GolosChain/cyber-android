package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.BytesCompress

@Abi
internal class ProfileMetadataAbi(
        val type: String?,
        val app: String?,
        val email: String?,
        val phone: String?,
        val facebook: String?,
        val instagram: String?,
        val telegram: String?,
        val vk: String?,
        val whatsapp: String?,
        val wechat: String?,
        val website: String?,
        val first_name: String?,
        val last_name: String?,
        val name: String?,
        val birth_date: String?,
        val gender: String?,
        val location: String?,
        val city: String?,
        val about: String?,
        val occupation: String?,
        val i_can: String?,
        val looking_for: String?,
        val business_category: String?,
        val background_image: String?,
        val cover_image: String?,
        val profile_image: String?,
        val user_image: String?,
        val ico_address: String?,
        val target_date: String?,
        val target_plan: String?,
        val target_point_a: String?,
        val target_point_b: String?) {


    val getType: ByteArray
        @BytesCompress get() = type.asOptionalStringBytes()

    val getApp: ByteArray
        @BytesCompress get() = app.asOptionalStringBytes()

    val getEmail: ByteArray
        @BytesCompress get() = email.asOptionalStringBytes()

    val getPhone: ByteArray
        @BytesCompress get() = phone.asOptionalStringBytes()

    val getFacebook: ByteArray
        @BytesCompress get() = facebook.asOptionalStringBytes()

    val getInstagramm: ByteArray
        @BytesCompress get() = instagram.asOptionalStringBytes()

    val getTelegramm: ByteArray
        @BytesCompress get() = telegram.asOptionalStringBytes()

    val getVk: ByteArray
        @BytesCompress get() = vk.asOptionalStringBytes()

    val getWhatsapp: ByteArray
        @BytesCompress get() = whatsapp.asOptionalStringBytes()

    val getWechat: ByteArray
        @BytesCompress get() =wechat.asOptionalStringBytes()

    val getWebsite: ByteArray
        @BytesCompress get() = website.asOptionalStringBytes()

    val getFirstName: ByteArray
        @BytesCompress get() = first_name.asOptionalStringBytes()

    val getLastName: ByteArray
        @BytesCompress get() = last_name.asOptionalStringBytes()

    val getName: ByteArray
        @BytesCompress get() = name.asOptionalStringBytes()

    val getBirthDate: ByteArray
        @BytesCompress get() = birth_date.asOptionalStringBytes()

    val getGender: ByteArray
        @BytesCompress get() = gender.asOptionalStringBytes()

    val getLocation: ByteArray
        @BytesCompress get() = location.asOptionalStringBytes()

    val getCity: ByteArray
        @BytesCompress get() = city.asOptionalStringBytes()

    val getAbout: ByteArray
        @BytesCompress get() = about.asOptionalStringBytes()

    val getOccupation: ByteArray
        @BytesCompress get() = occupation.asOptionalStringBytes()

    val getICan: ByteArray
        @BytesCompress get() = i_can.asOptionalStringBytes()

    val getLookingFor: ByteArray
        @BytesCompress get() = looking_for.asOptionalStringBytes()

    val getBusinessCategory: ByteArray
        @BytesCompress get() = business_category.asOptionalStringBytes()

    val getBackgroundImage: ByteArray
        @BytesCompress get() = background_image.asOptionalStringBytes()

    val getCoverImage: ByteArray
        @BytesCompress get() = cover_image.asOptionalStringBytes()

    val getProfileImage: ByteArray
        @BytesCompress get() = profile_image.asOptionalStringBytes()

    val getUserImage: ByteArray
        @BytesCompress get() = user_image.asOptionalStringBytes()

    val getIcoAddress: ByteArray
        @BytesCompress get() = ico_address.asOptionalStringBytes()

    val getTargetDate: ByteArray
        @BytesCompress get() = target_date.asOptionalStringBytes()

    val getTargetPlan: ByteArray
        @BytesCompress get() = target_plan.asOptionalStringBytes()

    val getTargetPoinA: ByteArray
        @BytesCompress get() = target_point_a.asOptionalStringBytes()

    val getTargetPoinB: ByteArray
        @BytesCompress get() = target_point_b.asOptionalStringBytes()


    override fun toString(): String {
        return "ProfileMetadataAbi{" +
                "type='" + type + '\''.toString() +
                ", app='" + app + '\''.toString() +
                ", email='" + email + '\''.toString() +
                ", phone='" + phone + '\''.toString() +
                ", facebook='" + facebook + '\''.toString() +
                ", instagram='" + instagram + '\''.toString() +
                ", telegram='" + telegram + '\''.toString() +
                ", vk='" + vk + '\''.toString() +
                ", website='" + website + '\''.toString() +
                ", first_name='" + first_name + '\''.toString() +
                ", last_name='" + last_name + '\''.toString() +
                ", name='" + name + '\''.toString() +
                ", birth_date='" + birth_date + '\''.toString() +
                ", gender='" + gender + '\''.toString() +
                ", location='" + location + '\''.toString() +
                ", city='" + city + '\''.toString() +
                ", about='" + about + '\''.toString() +
                ", occupation='" + occupation + '\''.toString() +
                ", i_can='" + i_can + '\''.toString() +
                ", looking_for='" + looking_for + '\''.toString() +
                ", business_category='" + business_category + '\''.toString() +
                ", background_image='" + background_image + '\''.toString() +
                ", cover_image='" + cover_image + '\''.toString() +
                ", profile_image='" + profile_image + '\''.toString() +
                ", user_image='" + user_image + '\''.toString() +
                ", ico_address='" + ico_address + '\''.toString() +
                ", target_date='" + target_date + '\''.toString() +
                ", target_plan='" + target_plan + '\''.toString() +
                ", target_point_a='" + target_point_a + '\''.toString() +
                ", target_point_b='" + target_point_b + '\''.toString() +
                '}'.toString()
    }
}