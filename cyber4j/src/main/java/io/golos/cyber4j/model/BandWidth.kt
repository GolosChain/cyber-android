package io.golos.cyber4j.model

import com.memtrip.eos.core.crypto.EosPrivateKey

enum class BandWidthSource {
    GOLOSIO_SERVICES, USING_KEY
}

data class BandWidthRequest @JvmOverloads constructor(val source: BandWidthSource,
                                                      val key: EosPrivateKey? = null) {
    companion object {
        val bandWidthFromGolosRequest = BandWidthRequest(BandWidthSource.GOLOSIO_SERVICES)

        fun EosPrivateKey.bandWidthRequest() = BandWidthRequest(BandWidthSource.USING_KEY, this)
    }
}