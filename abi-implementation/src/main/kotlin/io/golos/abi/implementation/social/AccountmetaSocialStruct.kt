// Class is generated, changes would be overridden on compile
package io.golos.abi.implementation.social

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NullableStringCompress
import com.memtrip.eos.abi.writer.compression.CompressionType
import com.memtrip.eos.chain.actions.transaction.abi.ActionAbi
import com.memtrip.eos.chain.actions.transaction.abi.TransactionAuthorizationAbi
import com.squareup.moshi.JsonClass
import io.golos.abi.implementation.AbiBinaryGenCyber
import io.golos.annotations.ForTechUse
import kotlin.String
import kotlin.collections.List

@Abi
@JsonClass(generateAdapter = true)
data class AccountmetaSocialStruct(
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
  val target_point_b: String?
) {
  val structName: String = "accountmeta"

  @ForTechUse
  val getType: String?
    @NullableStringCompress
    get() = type

  @ForTechUse
  val getApp: String?
    @NullableStringCompress
    get() = app

  @ForTechUse
  val getEmail: String?
    @NullableStringCompress
    get() = email

  @ForTechUse
  val getPhone: String?
    @NullableStringCompress
    get() = phone

  @ForTechUse
  val getFacebook: String?
    @NullableStringCompress
    get() = facebook

  @ForTechUse
  val getInstagram: String?
    @NullableStringCompress
    get() = instagram

  @ForTechUse
  val getTelegram: String?
    @NullableStringCompress
    get() = telegram

  @ForTechUse
  val getVk: String?
    @NullableStringCompress
    get() = vk

  @ForTechUse
  val getWhatsapp: String?
    @NullableStringCompress
    get() = whatsapp

  @ForTechUse
  val getWechat: String?
    @NullableStringCompress
    get() = wechat

  @ForTechUse
  val getWebsite: String?
    @NullableStringCompress
    get() = website

  @ForTechUse
  val getFirstName: String?
    @NullableStringCompress
    get() = first_name

  @ForTechUse
  val getLastName: String?
    @NullableStringCompress
    get() = last_name

  @ForTechUse
  val getName: String?
    @NullableStringCompress
    get() = name

  @ForTechUse
  val getBirthDate: String?
    @NullableStringCompress
    get() = birth_date

  @ForTechUse
  val getGender: String?
    @NullableStringCompress
    get() = gender

  @ForTechUse
  val getLocation: String?
    @NullableStringCompress
    get() = location

  @ForTechUse
  val getCity: String?
    @NullableStringCompress
    get() = city

  @ForTechUse
  val getAbout: String?
    @NullableStringCompress
    get() = about

  @ForTechUse
  val getOccupation: String?
    @NullableStringCompress
    get() = occupation

  @ForTechUse
  val getICan: String?
    @NullableStringCompress
    get() = i_can

  @ForTechUse
  val getLookingFor: String?
    @NullableStringCompress
    get() = looking_for

  @ForTechUse
  val getBusinessCategory: String?
    @NullableStringCompress
    get() = business_category

  @ForTechUse
  val getBackgroundImage: String?
    @NullableStringCompress
    get() = background_image

  @ForTechUse
  val getCoverImage: String?
    @NullableStringCompress
    get() = cover_image

  @ForTechUse
  val getProfileImage: String?
    @NullableStringCompress
    get() = profile_image

  @ForTechUse
  val getUserImage: String?
    @NullableStringCompress
    get() = user_image

  @ForTechUse
  val getIcoAddress: String?
    @NullableStringCompress
    get() = ico_address

  @ForTechUse
  val getTargetDate: String?
    @NullableStringCompress
    get() = target_date

  @ForTechUse
  val getTargetPlan: String?
    @NullableStringCompress
    get() = target_plan

  @ForTechUse
  val getTargetPointA: String?
    @NullableStringCompress
    get() = target_point_a

  @ForTechUse
  val getTargetPointB: String?
    @NullableStringCompress
    get() = target_point_b

  fun toHex() = AbiBinaryGenCyber(CompressionType.NONE)
                 .squishAccountmetaSocialStruct(this)
                 .toHex()
  fun toActionAbi(
    contractName: String,
    actionName: String,
    transactionAuth: List<TransactionAuthorizationAbi>
  ) = ActionAbi(contractName, actionName,
         transactionAuth, toHex())}
