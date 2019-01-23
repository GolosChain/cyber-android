package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.http.rpc.model.transaction.TransactionAuthorization

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

    constructor(actor: CommunName) : this(actor.name, AuthType.ACTIVE)
}