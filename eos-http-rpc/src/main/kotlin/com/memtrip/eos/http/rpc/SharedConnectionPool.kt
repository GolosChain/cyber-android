package com.memtrip.eos.http.rpc

object SharedConnectionPool {
    val pool = okhttp3.ConnectionPool()
}