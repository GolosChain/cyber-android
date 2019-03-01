package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.CollectionCompress
import com.memtrip.eos.abi.writer.DataCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
data class MyActionAbi(
        val account: String,
        val name: String,
        val authorization: List<MyTransactionAuthorizationAbi>,
        val data: String?
) {
    val getAccount: String
        @NameCompress get() = account

    val getName: String
        @NameCompress get() = name

    val getAuthorization: List<MyTransactionAuthorizationAbi>
        @CollectionCompress get() = authorization

    val getData: String?
        @DataCompress get() = data
}
