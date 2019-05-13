package io.golos.cyber4j.services.model;

import com.squareup.moshi.Json;

import io.golos.cyber4j.model.CyberName;
import io.golos.cyber4j.services.model.UserRegistrationState;

public class UserRegistrationStateResult {
    @Json(name = "currentState")
    private UserRegistrationState state;
    private CyberName user;


    public UserRegistrationStateResult(UserRegistrationState state, CyberName user) {
        this.state = state;
        this.user = user;
    }

    public CyberName getUser() {
        return user;
    }

    public void setUser(CyberName user) {
        this.user = user;
    }

    public UserRegistrationState getState() {
        return state;
    }

    public void setState(UserRegistrationState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "UserRegistrationStateResult{" +
                "state=" + state +
                ", name=" + user +
                '}';
    }
}
