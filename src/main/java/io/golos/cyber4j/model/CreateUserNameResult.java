package io.golos.cyber4j.model;

import java.util.Objects;

public class CreateUserNameResult {
    private CyberName creator;
    private CyberName owner;
    private String name;

    public CreateUserNameResult(CyberName creator, CyberName owner, String username) {
        this.creator = creator;
        this.owner = owner;
        this.name = username;
    }

    public CyberName getCreator() {
        return creator;
    }

    public void setCreator(CyberName creator) {
        this.creator = creator;
    }

    public CyberName getOwner() {
        return owner;
    }

    public void setOwner(CyberName owner) {
        this.owner = owner;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserNameResult that = (CreateUserNameResult) o;
        return Objects.equals(creator, that.creator) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, owner, name);
    }

    @Override
    public String toString() {
        return "CreateUserNameResult{" +
                "creator=" + creator +
                ", owner=" + owner +
                ", username='" + name + '\'' +
                '}';
    }
}
