package io.golos.cyber4j.services.model

enum class UserRegistrationState {
    REGISTERED,
    FIRST_STEP,
    VERIFY,
    SET_USER_NAME,
    TO_BLOCK_CHAIN;
}