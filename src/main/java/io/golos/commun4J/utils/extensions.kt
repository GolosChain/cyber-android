package io.golos.commun4J.utils


internal fun checkArgument(assertion: Boolean, errorMessage: String) {
    if (!assertion) throw IllegalArgumentException(errorMessage)
}