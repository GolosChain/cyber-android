package io.golos.cyber4j

import io.golos.cyber4j.utils.LogLevel
import okhttp3.logging.HttpLoggingInterceptor

data class Cyber4JConfig @JvmOverloads constructor(val blockChainHttpApiUrl: String = "http://46.4.96.246:8888/",// url of eos chain rest api
                                                   val servicesUrl: String = "ws://159.69.33.136:8080", //url of microservices gateway//"ws://159.69.33.136:8080"
                                                   val connectionTimeOutInSeconds: Int = 30,// time to wait unused socket or unresponsive http request to wait before drop
                                                   val readTimeoutInSeconds: Int = 30,// time to wait read from socket or  http request to wait before drop
                                                   val writeTimeoutInSeconds: Int = 30,// time to wait write from socket or  http request to wait before drop
                                                   val datePattern: String = "yyyy-MM-dd'T'HH:mm:ss",//blockchain date pattern, that it uses in various responses on transaction.
                                                                                                    // Unlikely to be changed;
                                                   val blockChainTimeZoneId: String = "GMT:0:00",//blockchain timezone. Used for proper converting between local and blockchain
                                                                                                //date time.  Unlikely to be changed;
                                                   val logLevel: LogLevel = LogLevel.BODY,// amount of logs. set LogLevel.NONE to disable
                                                   val httpLogger: HttpLoggingInterceptor.Logger? = null,
                                                   val socketLogger: HttpLoggingInterceptor.Logger? = null,
                                                   val performAutoAuthOnActiveUserSet : Boolean = true)
