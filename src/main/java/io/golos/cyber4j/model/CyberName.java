package io.golos.cyber4j.model;

import java.util.Objects;

import kotlin.text.Regex;

public class CyberName {

    private static final Regex pattern = new Regex("([A-Za-z0-9\\._-]+@[a-z]+)|([a-z0-5\\.]{0,12})");
    private String name;


    public CyberName(String name) {
        if (!pattern.matches(name))
            throw new IllegalStateException("invalid name " + name);
        this.name = name;
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
    public String toString() {
        return "CyberName{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CyberName cyberName = (CyberName) o;
        return Objects.equals(name, cyberName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
