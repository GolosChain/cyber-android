package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import io.golos.sharedmodel.CyberName

@Abi
internal class WitnessUnVoteRequestAbi(private val voter: CyberName,
                                       private val witness: CyberName) {

    val getVoter: String
        @NameCompress get() = voter.name

    val getWitness: String
        @NameCompress get() = witness.name
}