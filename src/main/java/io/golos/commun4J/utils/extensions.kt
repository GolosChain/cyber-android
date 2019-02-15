package io.golos.commun4J.utils

import io.golos.commun4J.model.CommunName


internal fun checkArgument(assertion: Boolean, errorMessage: String) {
    if (!assertion) throw IllegalArgumentException(errorMessage)
}

fun String.toCommunName() = CommunName(this)