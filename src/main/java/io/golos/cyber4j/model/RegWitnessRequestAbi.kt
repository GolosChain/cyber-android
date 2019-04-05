package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.PublicKeyCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class RegWitnessRequestAbi(private val witnessName: CyberName,
                                    private val witnessActiveKey: String,
                                    private val url: String) {
    val getName: String
        @NameCompress get() = witnessName.name

    val getPublicKey: String
        @PublicKeyCompress get() = witnessActiveKey

    val getUrl: String
        @StringCompress get() = url
}