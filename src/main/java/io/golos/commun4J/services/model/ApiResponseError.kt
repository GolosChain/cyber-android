package io.golos.commun4J.services.model

class ApiResponseError(val id: Long,
                       val error: Error) {

    class Error(val code: Long, val message: String) {
        override fun toString(): String {
            return "Error(code=$code, message='$message')"
        }
    }

    override fun toString(): String {
        return "ApiResponseError(id=$id, error=$error)"
    }
}