package io.golos.commun4J.utils

sealed class Either<S, F> {
    data class Success<S, F>(val value: S) : Either<S, F>()
    data class Failure<S, F>(val value: F) : Either<S, F>()
}

