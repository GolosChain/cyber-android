package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.http.rpc.model.transaction.TransactionAuthorization
import io.golos.sharedmodel.CyberName

@Abi
data class MyTransactionAuthorizationAbi(
        val actor: String,
        val permission: String
) {

    val getActor: String
        @NameCompress get() = actor

    val getPermission: String
        @NameCompress get() = permission

    constructor(authorizationAbi: TransactionAuthorization) : this(authorizationAbi.actor, authorizationAbi.permission)
    constructor(actor: String,
                permission: AuthType) : this(actor, permission.toString())

    constructor(actor: String) : this(actor, AuthType.ACTIVE)

    constructor(actor: CyberName) : this(actor.name, AuthType.ACTIVE)
}