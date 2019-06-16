package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress
import io.golos.sharedmodel.CyberName

@Abi
internal class RegWitnessRequestAbi(private val witnessName: CyberName,
                                    private val url: String) {
    val getName: String
        @NameCompress get() = witnessName.name

    val getUrl: String
        @StringCompress get() = url
}