package io.golos.cyber4j.model;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.Nullable;

public class ProfileMetadataUpdateResult {
    private String name;
    private ResultProfileMetadata meta;

    public ProfileMetadataUpdateResult(String name, ResultProfileMetadata metadata) {
        this.name = name;
        this.meta = metadata;
    }

    public ProfileMetadataUpdateResult() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMetadata(ResultProfileMetadata metadata) {
        this.meta = metadata;
    }

    public String getName() {
        return name;
    }

    public ResultProfileMetadata getMetadata() {
        return meta;
    }

    @Override
    public String toString() {
        return "ProfileMetadataUpdateResult{" +
                "name='" + name + '\'' +
                ", metadata=" + meta +
                '}';
    }

   public static class ResultProfileMetadata {
        @Json(name = "type")
        @Nullable
        private String type;
        @Nullable
        @Json(name = "app")
        private String app;
        @Nullable
        @Json(name = "email")
        private String email;
        @Nullable
        @Json(name = "phone")
        private String phone;
        @Nullable
        @Json(name = "facebook")
        private String facebook;
        @Nullable
        @Json(name = "instagram")
        private String instagram;
        @Nullable
        @Json(name = "telegram")
        private String telegram;
        @Nullable
        @Json(name = "vk")
        private String vk;
        @Nullable
        @Json(name = "website")
        private String website;
        @Nullable
        @Json(name = "first_name")
        private String firstName;
        @Nullable
        @Json(name = "last_name")
        private String lastName;
        @Nullable
        @Json(name = "name")
        private String name;
        @Nullable
        @Json(name = "birth_date")
        private String birthDate;
        @Nullable
        @Json(name = "gender")
        private String gender;
        @Nullable
        @Json(name = "location")
        private String location;
        @Nullable
        @Json(name = "city")
        private String city;
        @Nullable
        @Json(name = "about")
        private String about;
        @Nullable
        @Json(name = "occupation")
        private String occupation;
        @Nullable
        @Json(name = "i_can")
        private String iCan;
        @Nullable
        @Json(name = "looking_for")
        private String lookingFor;
        @Nullable
        @Json(name = "business_category")
        private String businessCategory;
        @Nullable
        @Json(name = "background_image")
        private String backgroundImage;
        @Nullable
        @Json(name = "cover_image")
        private String coverImage;
        @Nullable
        @Json(name = "profile_image")
        private String profileImage;
        @Nullable
        @Json(name = "user_image")
        private String userImage;
        @Nullable
        @Json(name = "ico_address")
        private String icoAddress;
        @Nullable
        @Json(name = "target_date")
        private String targetDate;
        @Nullable
        @Json(name = "target_plan")
        private String targetPlan;
        @Nullable
        @Json(name = "target_point_a")
        private String targetPointA;
        @Nullable
        @Json(name = "target_point_b")
        private String targetPointB;

        public ResultProfileMetadata(@Nullable String type, @Nullable String app, @Nullable String email, @Nullable String phone, @Nullable String facebook, @Nullable String instagram, @Nullable String telegram, @Nullable String vk, @Nullable String website, @Nullable String firstName, @Nullable String lastName, @Nullable String name, @Nullable String birthDate, @Nullable String gender, @Nullable String location, @Nullable String city, @Nullable String about, @Nullable String occupation, @Nullable String iCan, @Nullable String lookingFor, @Nullable String businessCategory, @Nullable String backgroundImage, @Nullable String coverImage, @Nullable String profileImage, @Nullable String userImage, @Nullable String icoAddress, @Nullable String targetDate, @Nullable String targetPlan, @Nullable String targetPointA, @Nullable String targetPointB) {
            this.type = type;
            this.app = app;
            this.email = email;
            this.phone = phone;
            this.facebook = facebook;
            this.instagram = instagram;
            this.telegram = telegram;
            this.vk = vk;
            this.website = website;
            this.firstName = firstName;
            this.lastName = lastName;
            this.name = name;
            this.birthDate = birthDate;
            this.gender = gender;
            this.location = location;
            this.city = city;
            this.about = about;
            this.occupation = occupation;
            this.iCan = iCan;
            this.lookingFor = lookingFor;
            this.businessCategory = businessCategory;
            this.backgroundImage = backgroundImage;
            this.coverImage = coverImage;
            this.profileImage = profileImage;
            this.userImage = userImage;
            this.icoAddress = icoAddress;
            this.targetDate = targetDate;
            this.targetPlan = targetPlan;
            this.targetPointA = targetPointA;
            this.targetPointB = targetPointB;
        }

        public ResultProfileMetadata() {
        }

        @Nullable
        public String getType() {
            return type;
        }

        public void setType(@Nullable String type) {
            this.type = type;
        }

        @Nullable
        public String getApp() {
            return app;
        }

        public void setApp(@Nullable String app) {
            this.app = app;
        }

        @Nullable
        public String getEmail() {
            return email;
        }

        public void setEmail(@Nullable String email) {
            this.email = email;
        }

        @Nullable
        public String getPhone() {
            return phone;
        }

        public void setPhone(@Nullable String phone) {
            this.phone = phone;
        }

        @Nullable
        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(@Nullable String facebook) {
            this.facebook = facebook;
        }

        @Nullable
        public String getInstagram() {
            return instagram;
        }

        public void setInstagram(@Nullable String instagram) {
            this.instagram = instagram;
        }

        @Nullable
        public String getTelegram() {
            return telegram;
        }

        public void setTelegram(@Nullable String telegram) {
            this.telegram = telegram;
        }

        @Nullable
        public String getVk() {
            return vk;
        }

        public void setVk(@Nullable String vk) {
            this.vk = vk;
        }

        @Nullable
        public String getWebsite() {
            return website;
        }

        public void setWebsite(@Nullable String website) {
            this.website = website;
        }

        @Nullable
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(@Nullable String firstName) {
            this.firstName = firstName;
        }

        @Nullable
        public String getLastName() {
            return lastName;
        }

        public void setLastName(@Nullable String lastName) {
            this.lastName = lastName;
        }

        @Nullable
        public String getName() {
            return name;
        }

        public void setName(@Nullable String name) {
            this.name = name;
        }

        @Nullable
        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(@Nullable String birthDate) {
            this.birthDate = birthDate;
        }

        @Nullable
        public String getGender() {
            return gender;
        }

        public void setGender(@Nullable String gender) {
            this.gender = gender;
        }

        @Nullable
        public String getLocation() {
            return location;
        }

        public void setLocation(@Nullable String location) {
            this.location = location;
        }

        @Nullable
        public String getCity() {
            return city;
        }

        public void setCity(@Nullable String city) {
            this.city = city;
        }

        @Nullable
        public String getAbout() {
            return about;
        }

        public void setAbout(@Nullable String about) {
            this.about = about;
        }

        @Nullable
        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(@Nullable String occupation) {
            this.occupation = occupation;
        }

        @Nullable
        public String getiCan() {
            return iCan;
        }

        public void setiCan(@Nullable String iCan) {
            this.iCan = iCan;
        }

        @Nullable
        public String getLookingFor() {
            return lookingFor;
        }

        public void setLookingFor(@Nullable String lookingFor) {
            this.lookingFor = lookingFor;
        }

        @Nullable
        public String getBusinessCategory() {
            return businessCategory;
        }

        public void setBusinessCategory(@Nullable String businessCategory) {
            this.businessCategory = businessCategory;
        }

        @Nullable
        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(@Nullable String backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Nullable
        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(@Nullable String coverImage) {
            this.coverImage = coverImage;
        }

        @Nullable
        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(@Nullable String profileImage) {
            this.profileImage = profileImage;
        }

        @Nullable
        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(@Nullable String userImage) {
            this.userImage = userImage;
        }

        @Nullable
        public String getIcoAddress() {
            return icoAddress;
        }

        public void setIcoAddress(@Nullable String icoAddress) {
            this.icoAddress = icoAddress;
        }

        @Nullable
        public String getTargetDate() {
            return targetDate;
        }

        public void setTargetDate(@Nullable String targetDate) {
            this.targetDate = targetDate;
        }

        @Nullable
        public String getTargetPlan() {
            return targetPlan;
        }

        public void setTargetPlan(@Nullable String targetPlan) {
            this.targetPlan = targetPlan;
        }

        @Nullable
        public String getTargetPointA() {
            return targetPointA;
        }

        public void setTargetPointA(@Nullable String targetPointA) {
            this.targetPointA = targetPointA;
        }

        @Nullable
        public String getTargetPointB() {
            return targetPointB;
        }

        public void setTargetPointB(@Nullable String targetPointB) {
            this.targetPointB = targetPointB;
        }

        @Override
        public String toString() {
            return "ProfileMetadataAbi{" +
                    "type='" + type + '\'' +
                    ", app='" + app + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    ", facebook='" + facebook + '\'' +
                    ", instagram='" + instagram + '\'' +
                    ", telegram='" + telegram + '\'' +
                    ", vk='" + vk + '\'' +
                    ", website='" + website + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", name='" + name + '\'' +
                    ", birthDate='" + birthDate + '\'' +
                    ", gender='" + gender + '\'' +
                    ", location='" + location + '\'' +
                    ", city='" + city + '\'' +
                    ", about='" + about + '\'' +
                    ", occupation='" + occupation + '\'' +
                    ", iCan='" + iCan + '\'' +
                    ", lookingFor='" + lookingFor + '\'' +
                    ", businessCategory='" + businessCategory + '\'' +
                    ", backgroundImage='" + backgroundImage + '\'' +
                    ", coverImage='" + coverImage + '\'' +
                    ", profileImage='" + profileImage + '\'' +
                    ", userImage='" + userImage + '\'' +
                    ", icoAddress='" + icoAddress + '\'' +
                    ", targetDate='" + targetDate + '\'' +
                    ", targetPlan='" + targetPlan + '\'' +
                    ", targetPointA='" + targetPointA + '\'' +
                    ", targetPointB='" + targetPointB + '\'' +
                    '}';
        }
    }
}
