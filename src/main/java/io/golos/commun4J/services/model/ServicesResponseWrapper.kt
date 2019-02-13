package io.golos.commun4J.services.model

internal class ServicesResponseWrapper<T>(val id: Long, val result: T)

internal class Identifieble(val id: Long?)