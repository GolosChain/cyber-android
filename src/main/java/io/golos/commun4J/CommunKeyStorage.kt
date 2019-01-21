package io.golos.commun4J

import com.memtrip.eos.core.crypto.EosPrivateKey
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import java.util.*
import kotlin.collections.HashMap

object CommunKeyStorage {


    private var activeAccount: CommunName? = null

    private val accounts = Collections.synchronizedMap(HashMap<CommunName, Set<Pair<AuthType, String>>>())

    @Synchronized
    fun getActiveAccountKeys(): Set<Pair<AuthType, String>> {
        val activeAcc = io.golos.commun4J.CommunKeyStorage.activeAccount
                ?: throw java.lang.IllegalStateException("active account not set")
        return io.golos.commun4J.CommunKeyStorage.accounts[activeAcc]!!
    }

    @Synchronized
    fun getActiveAccount(): CommunName {
        return io.golos.commun4J.CommunKeyStorage.activeAccount
                ?: throw java.lang.IllegalStateException("active aacount not set")
    }

    fun getAccountKeys(accName: CommunName) = io.golos.commun4J.CommunKeyStorage.accounts[accName]

    fun addAccountKeys(accName: CommunName, keys: Set<Pair<AuthType, String>>) {
        keys.forEach {
            EosPrivateKey(it.second)
        }
        synchronized(this) {
            if (io.golos.commun4J.CommunKeyStorage.activeAccount == null) io.golos.commun4J.CommunKeyStorage.activeAccount = accName
            val oldKeys = io.golos.commun4J.CommunKeyStorage.accounts[accName]
            val resultingKeys = keys + oldKeys.orEmpty()
            io.golos.commun4J.CommunKeyStorage.accounts[accName] = resultingKeys
        }
    }

    @Synchronized
    fun setAccountActive(name: CommunName) {
        io.golos.commun4J.CommunKeyStorage.accounts[name].takeIf { it == null }?.let { throw IllegalStateException("no keys for $name account name") }
        io.golos.commun4J.CommunKeyStorage.activeAccount = name
    }
}