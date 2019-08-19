package io.golos.cyber4j.http.rpc

object SharedConnectionPool {
    val pool = okhttp3.ConnectionPool()
}