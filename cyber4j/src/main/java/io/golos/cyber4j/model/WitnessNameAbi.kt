package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import io.golos.sharedmodel.CyberName

@Abi
class WitnessNameAbi(private val witnessName: CyberName) {
    val getName: String
        @NameCompress get() = witnessName.name
}