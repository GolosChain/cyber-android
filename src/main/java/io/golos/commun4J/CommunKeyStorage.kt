package io.golos.commun4J

import com.memtrip.eos.core.crypto.EosPrivateKey
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import io.golos.commun4J.utils.Pair
import java.util.*
import kotlin.collections.HashMap

open class CommunKeyStorage {

    private var activeAccount: CommunName? = null

    private val accounts = Collections.synchronizedMap(HashMap<CommunName, Set<Pair<AuthType, String>>>())

    @Synchronized
    fun getActiveAccountKeys(): Set<Pair<AuthType, String>> {
        val activeAcc = activeAccount
                ?: throw java.lang.IllegalStateException("active account not set")
        return accounts[activeAcc]!!
    }

    @Synchronized
    fun getActiveAccount(): CommunName {
        return activeAccount
                ?: throw java.lang.IllegalStateException("active aacount not set")
    }

    fun getAccountKeys(accName: CommunName) = accounts[accName]

    fun addAccountKeys(accName: CommunName, keys: Set<Pair<AuthType, String>>) {
        keys.forEach {
            EosPrivateKey(it.second)
        }
        synchronized(this) {
            if (activeAccount == null) activeAccount = accName
            val oldKeys = accounts[accName]
            val resultingKeys = keys + oldKeys.orEmpty()
            accounts[accName] = resultingKeys
        }
    }

    @Synchronized
    fun setAccountActive(name: CommunName) {
        accounts[name].takeIf { it == null }?.let { throw IllegalStateException("no keys for $name account name") }
        activeAccount = name
    }
}