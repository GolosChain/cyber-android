package io.golos.commun4J

import io.golos.commun4J.utils.LogLevel

data class Commun4JConfig(val connectionUrl: String = "http://46.4.96.246:8888/",
                          val servicesUrl: String = "wss://echo.websocket.org", //"ws://dev-gate.golos.io",
                          val connectionTimeOutInSeconds: Int = 30,
                          val readTimeoutInSeconds: Int = 30,
                          val writeTimeoutInSeconds: Int = 30,
                          val datePattern: String = "yyyy-MM-dd'T'HH:mm:ss",
                          val timeZoneId: String = "GMT:0:00",
                          val logLevel: LogLevel = LogLevel.BODY)