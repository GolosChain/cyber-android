package io.golos.cyber4j.model;

import com.squareup.moshi.Json;

public class UserRegistrationStateResult {
    @Json(name = "currentState")
    private UserRegistrationState state;

    public UserRegistrationStateResult(UserRegistrationState state) {
        this.state = state;
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
                '}';
    }
}
