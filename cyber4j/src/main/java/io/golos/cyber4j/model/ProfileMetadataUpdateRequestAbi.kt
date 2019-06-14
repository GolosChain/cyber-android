package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.ChildCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
internal class ProfileMetadataUpdateRequestAbi(private val name: CyberName,
                                               private val metadata: ProfileMetadataAbi) {

    val getName: String
        @NameCompress get() = name.name

    val getMetadata: ProfileMetadataAbi
        @ChildCompress get() = metadata


    override fun toString(): String {
        return "ProfileMetadataUpdateRequestAbi{" +
                "name='" + name + '\''.toString() +
                ", metadata=" + metadata +
                '}'.toString()
    }
}
