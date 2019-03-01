package io.golos.cyber4j.model;

import kotlin.text.Regex;

public class CyberName {

    private static final Regex pattern = new Regex("[a-z0-5.]{0,12}");
    private String name;


    public CyberName(String name) {
        if (!pattern.matches(name))
            throw new IllegalStateException("name must consist only of a-z and 1-5 characters, " +
                    "and be no longer then 12 chars, set name is " + name);
        this.name = name;
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


}
