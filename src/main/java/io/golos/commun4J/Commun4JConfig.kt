package io.golos.commun4J

import io.golos.commun4J.utils.LogLevel

data class Commun4JConfig @JvmOverloads constructor(val blockChainHttpApiUrl: String = "http://46.4.96.246:8888/",
                                                    val isPrivateTestNet : Boolean = false,
                                                    val servicesUrl: String = "wss://dev-gate.golos.io", //"ws://dev-gate.golos.io",
                                                    val connectionTimeOutInSeconds: Int = 30,
                                                    val readTimeoutInSeconds: Int = 30,
                                                    val writeTimeoutInSeconds: Int = 30,
                                                    val datePattern: String = "yyyy-MM-dd'T'HH:mm:ss",
                                                    val blockChainTimeZoneId: String = "GMT:0:00",
                                                    val logLevel: LogLevel = LogLevel.BODY)
