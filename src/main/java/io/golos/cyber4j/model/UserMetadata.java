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
    private Subscribers subscribers;
    private Date createdAt;
    private boolean isSubscribed;

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public Subscribers getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Subscribers subscribers) {
        this.subscribers = subscribers;
    }

    public UserSubscriptions getSubscriptions() {
        return subscriptions;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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


    public static class Subscribers {
        private int usersCount;
        private int communitiesCount;

        public int getUsersCount() {
            return usersCount;
        }

        public void setUsersCount(int usersCount) {
            this.usersCount = usersCount;
        }

        public int getCommunitiesCount() {
            return communitiesCount;
        }

        public void setCommunitiesCount(int communitiesCount) {
            this.communitiesCount = communitiesCount;
        }

    }

    public static class SubsribedUser {
        private String id;
        private String avatarUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

    }

    public static class UserPersonalData {
        @Nullable
        private String avatarUrl;
        @Nullable
        private String coverUrl;
        @Nullable
        private String biography;
        @Nullable
        private Contacts contacts;

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
    }

    public static class Contacts {
        @Nullable
        private String facebook;
        @Nullable
        private String telegram;
        @Nullable
        private String whatsApp;
        @Nullable
        private String weChat;

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

    public static class UserRegistration {
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

    }

    public static class UserStats {
        private long postsCount;
        private long commentsCount;

        public long getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(long commentsCount) {
            this.commentsCount = commentsCount;
        }

        public UserStats(long postsCount) {
            this.postsCount = postsCount;
        }

        public long getPostsCount() {
            return postsCount;
        }

        public void setPostsCount(long postsCount) {
            this.postsCount = postsCount;
        }

    }

    public static class UserSubscriptions {
        private int usersCount;
        private int communitiesCount;

        public int getUsersCount() {
            return usersCount;
        }


        public void setUsersCount(int usersCount) {
            this.usersCount = usersCount;
        }

        public int getCommunitiesCount() {
            return communitiesCount;
        }

        public void setCommunitiesCount(int communitiesCount) {
            this.communitiesCount = communitiesCount;
        }


        }
    }

