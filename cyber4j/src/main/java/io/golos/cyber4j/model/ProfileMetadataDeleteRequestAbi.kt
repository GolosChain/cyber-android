package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class ProfileMetadataDeleteRequestAbi(val name: CyberName) {
    val getName: String
        @NameCompress get() = name.name

    override fun toString(): String {
        return "ProfileMetadataDeleteRequestAbi(name=$name)"
    }
}