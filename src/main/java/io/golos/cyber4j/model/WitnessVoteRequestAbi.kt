package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.ShortCompress

@Abi
internal class WitnessVoteRequestAbi(private val voter: CyberName,
                                     private val witness: CyberName,
                                     private val weight: Short) {

    val getVoter: String
        @NameCompress get() = voter.name

    val getWitness: String
        @NameCompress get() = witness.name

    val getWeight: Short
        @ShortCompress get() = weight
}