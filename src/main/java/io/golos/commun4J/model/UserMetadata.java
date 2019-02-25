package io.golos.commun4J.model;

import org.jetbrains.annotations.Nullable;

//personal: {
//        avatarUrl: {
//        type: String,
//        },
//        coverUrl: {
//        type: String,
//        },
//        biography: {
//        type: String,
//        },
//        contacts: {
//        facebook: {
//        type: String,
//        },
//        telegram: {
//        type: String,
//        },
//        whatsApp: {
//        type: String,
//        },
//        weChat: {
//        type: String,
//        },
//        },
//        },
public class UserMetadata {
    @Nullable private String avatarUrl;
    @Nullable private String coverUrl;
    @Nullable private String biography;
    @Nullable private Contacts contacts;

    public UserMetadata(@Nullable String avatarUrl, @Nullable String coverUrl, @Nullable String biography, @Nullable Contacts contacts) {
        this.avatarUrl = avatarUrl;
        this.coverUrl = coverUrl;
        this.biography = biography;
        this.contacts = contacts;
    }

    @Nullable
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(@Nullable String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Nullable
    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(@Nullable String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Nullable
    public String getBiography() {
        return biography;
    }

    public void setBiography(@Nullable String biography) {
        this.biography = biography;
    }

    @Nullable
    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(@Nullable Contacts contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "UserMetadata{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", biography='" + biography + '\'' +
                ", contacts=" + contacts +
                '}';
    }

    static class Contacts{
        @Nullable private String facebook;
        @Nullable private String telegram;
        @Nullable private String whatsApp;
        @Nullable private String weChat;

        public Contacts(@Nullable String facebook, @Nullable String telegram, @Nullable String whatsApp, @Nullable String weChat) {
            this.facebook = facebook;
            this.telegram = telegram;
            this.whatsApp = whatsApp;
            this.weChat = weChat;
        }

        @Nullable
        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(@Nullable String facebook) {
            this.facebook = facebook;
        }

        @Nullable
        public String getTelegram() {
            return telegram;
        }

        public void setTelegram(@Nullable String telegram) {
            this.telegram = telegram;
        }

        @Nullable
        public String getWhatsApp() {
            return whatsApp;
        }

        public void setWhatsApp(@Nullable String whatsApp) {
            this.whatsApp = whatsApp;
        }

        @Nullable
        public String getWeChat() {
            return weChat;
        }

        public void setWeChat(@Nullable String weChat) {
            this.weChat = weChat;
        }

        @Override
        public String toString() {
            return "Contacts{" +
                    "facebook='" + facebook + '\'' +
                    ", telegram='" + telegram + '\'' +
                    ", whatsApp='" + whatsApp + '\'' +
                    ", weChat='" + weChat + '\'' +
                    '}';
        }
    }

}
