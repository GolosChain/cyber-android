package io.golos.cyber4j.model;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import kotlin.text.Regex;

public class CyberName {

    private static final Regex pattern = new Regex("([A-Za-z0-9\\._-]+@[a-z]+)|([a-z0-5\\.]{0,12})");
    private static final Regex canonicalNamePattern = new Regex("[a-z0-5\\.]{0,12}");
    private static final Regex domainNamePatter = new Regex("[A-Za-z0-9\\._-]+@[a-z]+");
    private String name;
    @Nullable
    private String domainName;


    public CyberName(String name) {
        if (!pattern.matches(name))
            throw new IllegalStateException("invalid name " + name);
        this.name = name;
        if (name.contains("@")) domainName = name;
    }

    public CyberName(String canonicalName, String domainName) {
        if (!domainNamePatter.matches(domainName))
            throw new IllegalStateException("invalid domainName name " + domainName);
        this.domainName = domainName;

        if (!canonicalNamePattern.matches(canonicalName))
            throw new IllegalStateException("invalid canonical name " + domainName);
        this.name = canonicalName;
    }

    @Nullable
    public String getDomainName() {
        return domainName;
    }

    public boolean isCanonicalName() {
        return !name.contains("@");
    }

    public CyberName() {
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CyberName cyberName = (CyberName) o;
        return Objects.equals(name, cyberName.name) &&
                Objects.equals(domainName, cyberName.domainName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domainName);
    }

    @Override
    public String toString() {
        return "CyberName{" +
                "name='" + name + '\'' +
                ", domainName='" + domainName + '\'' +
                '}';
    }
}
