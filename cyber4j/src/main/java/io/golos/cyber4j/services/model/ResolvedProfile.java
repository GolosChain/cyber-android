package io.golos.cyber4j.services.model;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import io.golos.cyber4j.model.CyberName;

public class ResolvedProfile {
    private CyberName userId;
    private String username;
    @Nullable
    private String avatarUrl;

    public ResolvedProfile(CyberName userId, String username, @Nullable String avatarUrl) {
        this.userId = userId;
        this.username = username;
        this.avatarUrl = avatarUrl;
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

    @Nullable
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(@Nullable String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResolvedProfile that = (ResolvedProfile) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(avatarUrl, that.avatarUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, avatarUrl);
    }
}
