package io.golos.cyber4j.model;

import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

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
    private UserPersonalData personal;
    private UserSubscriptions subscriptions;
    private UserStats stats;
    private CyberName userId;
    private String username;
    private UserRegistration registration;


    public UserSubscriptions getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(UserSubscriptions subscriptions) {
        this.subscriptions = subscriptions;
    }

    public CyberName getUserId() {
        return userId;
    }

    public void setUserId(CyberName userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStats getStats() {
        return stats;
    }

    public void setStats(UserStats stats) {
        this.stats = stats;
    }


    public UserPersonalData getPersonal() {
        return personal;
    }

    public void setPersonal(UserPersonalData personal) {
        this.personal = personal;
    }

    public UserRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(UserRegistration registration) {
        this.registration = registration;
    }

    public UserMetadata(UserPersonalData personal, UserSubscriptions subscriptions, UserStats stats, CyberName userId, String username, UserRegistration registration) {
        this.personal = personal;
        this.subscriptions = subscriptions;
        this.stats = stats;
        this.userId = userId;
        this.username = username;
        this.registration = registration;
    }

    @Override
    public String toString() {
        return "UserMetadata{" +
                "personal=" + personal +
                ", subscriptions=" + subscriptions +
                ", stats=" + stats +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", registration=" + registration + "}";
    }

    static class UserPersonalData {
        @Nullable private String avatarUrl;
        @Nullable private String coverUrl;
        @Nullable private String biography;
        @Nullable private Contacts contacts;

        public UserPersonalData(@Nullable String avatarUrl, @Nullable String coverUrl, @Nullable String biography, @Nullable Contacts contacts) {
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
            return "UserPersonalData{" +
                    "avatarUrl='" + avatarUrl + '\'' +
                    ", coverUrl='" + coverUrl + '\'' +
                    ", biography='" + biography + '\'' +
                    ", contacts=" + contacts +
                    '}';
        }
    }

    static class Contacts {
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

    static class UserRegistration {
        private Date time;

        public UserRegistration(Date time) {
            this.time = time;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "UserRegistration{" +
                    "time=" + time +
                    '}';
        }
    }

    static class UserStats {
        private long postsCount;

        public UserStats(long postsCount) {
            this.postsCount = postsCount;
        }

        public long getPostsCount() {
            return postsCount;
        }

        public void setPostsCount(long postsCount) {
            this.postsCount = postsCount;
        }

        @Override
        public String toString() {
            return "UserStats{" +
                    "postsCount=" + postsCount +
                    '}';
        }
    }

    static class UserSubscriptions {
        private List<CyberCommunity> communities;

        public UserSubscriptions(List<CyberCommunity> communities) {
            this.communities = communities;
        }

        public List<CyberCommunity> getCommunities() {
            return communities;
        }

        public void setCommunities(List<CyberCommunity> communities) {
            this.communities = communities;
        }

        @Override
        public String toString() {
            return "UserSubscriptions{" +
                    "communities=" + communities +
                    '}';
        }
    }
}
