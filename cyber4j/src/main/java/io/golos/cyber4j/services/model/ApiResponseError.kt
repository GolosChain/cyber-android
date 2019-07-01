package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseError(val id: Long,
                            val error: ErrorDetails)

@JsonClass(generateAdapter = true)
class ErrorDetails(val code: Long, val message: String) {
    override fun toString(): String {
        return "ErrorDetails(code=$code, message='$message')"
    }
}