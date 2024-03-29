package io.golos.cyber4j.services.model

import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class FirstRegistrationStepResult(val code: Int?,
                                       val strategy: RegistrationStrategy,
                                       val nextSmsRetry: Date)
