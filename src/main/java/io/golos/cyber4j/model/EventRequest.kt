package io.golos.cyber4j.model

internal class MarkAsReadRequest(val ids: List<String>)

internal class MarkAllReadRequest

internal class GetUnreadCountRequest(val profile: String)

class FreshResult(val fresh: Int)