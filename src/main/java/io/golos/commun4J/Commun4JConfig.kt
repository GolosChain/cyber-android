package io.golos.commun4J

data class Commun4JConfig(val connectionUrl: String = "http://46.4.96.246:8888/",
                          val connectionTimeOutInSeconds: Int = 3,
                          val readTimeoutInSeconds: Int = 3,
                          val writeTimeoutInSeconds: Int = 3,
                          val datePattern: String = "yyyy-MM-dd'T'HH:mm:ss",
                          val timeZoneId: String = "GMT:0:00")