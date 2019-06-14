package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress

@Abi
internal class CreateUserNameAbi(private val creator: CyberName,
                                 private val owner: CyberName,
                                 private val newUserName: String) {
    val getCreator: String
        @NameCompress get() = creator.name

    val getOwner: String
        @NameCompress get() = owner.name

    val getNewUserName: String
        @StringCompress get() = newUserName
}