package io.golos.commun4J

import com.memtrip.eos.core.crypto.EosPrivateKey
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import io.golos.commun4J.utils.Pair
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

interface OnKeysAddedListener {
    fun onActiveKeysAdded(newUser: CommunName, activeKey: String, oldUser: CommunName?)
}

open class CommunKeyStorage {

    private var activeAccount: CommunName? = null

    private val accounts = Collections.synchronizedMap(HashMap<CommunName, Set<Pair<AuthType, String>>>())

    private val keyListeners = ArrayList<OnKeysAddedListener>()

    @Synchronized
    fun getActiveAccountKeys(): Set<Pair<AuthType, String>> {
        val activeAcc = activeAccount
                ?: throw java.lang.IllegalStateException("active account not set")
        return accounts[activeAcc]!!
    }


    fun addOnKeyChangedListener(listener: OnKeysAddedListener) {
        synchronized(keyListeners) {
            keyListeners.add(listener)
        }
    }

    @Synchronized
    fun getActiveAccount(): CommunName {
        return activeAccount
                ?: throw java.lang.IllegalStateException("active aacount not set")
    }

    @Synchronized
    fun isActiveAccountSet() = activeAccount != null

    fun getAccountKeys(accName: CommunName) = accounts[accName]

    fun addAccountKeys(accName: CommunName, keys: Set<Pair<AuthType, String>>) {
        keys.forEach {
            EosPrivateKey(it.second)
        }
        synchronized(this) {
            val oldKeys = accounts[accName]
            val resultingKeys = keys + oldKeys.orEmpty()
            accounts[accName] = resultingKeys
            setAccountActive(accName)
        }
    }

    @Synchronized
    fun setAccountActive(name: CommunName) {
        accounts[name].takeIf { it == null }?.let { throw IllegalStateException("no keys for $name account name") }
        val oldUser = activeAccount
        activeAccount = name

        val activeKey = getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: return
        keyListeners.forEach {
            it.onActiveKeysAdded(name, activeKey, oldUser)
        }
    }
}