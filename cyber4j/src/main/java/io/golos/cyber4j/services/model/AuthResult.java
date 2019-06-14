package io.golos.cyber4j.services.model;

import java.util.Arrays;
import java.util.Objects;

import io.golos.cyber4j.model.CyberName;

public class AuthResult {
    private CyberName user;
    private String displayName;
    private String[] roles;
    private String permission;

    public AuthResult(CyberName user, String displayName, String[] roles, String permission) {
        this.user = user;
        this.displayName = displayName;
        this.roles = roles;
        this.permission = permission;
    }

    public CyberName getUser() {
        return user;
    }

    public void setUser(CyberName user) {
        this.user = user;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthResult that = (AuthResult) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(displayName, that.displayName) &&
                Arrays.equals(roles, that.roles) &&
                Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(user, displayName, permission);
        result = 31 * result + Arrays.hashCode(roles);
        return result;
    }
}
