package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class WitnessVoteRequestAbi(private val voter: CyberName,
                                     private val witness: CyberName) {

    val getVoter: String
        @NameCompress get() = voter.name

    val getWitness: String
        @NameCompress get() = witness.name
}