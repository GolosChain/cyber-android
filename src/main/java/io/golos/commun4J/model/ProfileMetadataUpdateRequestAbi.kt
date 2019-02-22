package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class ProfileMetadataUpdateRequestAbi(private val name: CommunName,
                                               private val metadata: ProfileMetadata) {

    val getName: String
        @NameCompress get() = name.name

    val getMetadata: ProfileMetadata
        @ChildCompress get() = metadata


    override fun toString(): String {
        return "ProfileMetadataUpdateRequestAbi{" +
                "name='" + name + '\''.toString() +
                ", metadata=" + metadata +
                '}'.toString()
    }
}
