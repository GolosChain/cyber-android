package io.golos.commun4J

sealed class Either<S, F> {
    data class Success<S, F>(val value: S) : io.golos.commun4J.Either<S, F>()
    data class Failure<S, F>(val value: F) : io.golos.commun4J.Either<S, F>()
}

