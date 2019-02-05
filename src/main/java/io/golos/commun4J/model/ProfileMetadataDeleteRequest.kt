package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress

@Abi
class ProfileMetadataDeleteRequest(val name: CommunName) {
    val getName: String
        @NameCompress get() = name.name

    override fun toString(): String {
        return "ProfileMetadataDeleteRequest(name=$name)"
    }
}