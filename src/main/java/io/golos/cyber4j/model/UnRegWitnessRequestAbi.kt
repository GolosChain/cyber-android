package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class UnRegWitnessRequestAbi(private val witnessName: CyberName) {
    val getName: String
        @NameCompress get() = witnessName.name
}