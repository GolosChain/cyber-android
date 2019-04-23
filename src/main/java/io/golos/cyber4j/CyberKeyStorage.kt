package io.golos.cyber4j

import com.memtrip.eos.core.crypto.EosPrivateKey
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.Pair
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

interface OnKeysAddedListener {
    fun onActiveKeysAdded(newUser: CyberName, activeKey: String, oldUser: CyberName?)
}

interface ResolvedUserNamesProvider {
    fun resolveCanonicalCyberName(cyberName: CyberName): CyberName
}

class KeyStorage {

    private var activeAccount: CyberName? = null

    var resolvedUserNamesProvider: ResolvedUserNamesProvider? = null

    private val accounts = Collections.synchronizedMap(HashMap<CyberName, Set<Pair<AuthType, String>>>())

    private val keyListeners = ArrayList<OnKeysAddedListener>()

    private fun CyberName.resolveAccountName() = resolvedUserNamesProvider?.resolveCanonicalCyberName(this)
            ?: this

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
    fun getActiveAccount(): CyberName {
        return activeAccount
                ?: throw java.lang.IllegalStateException("active account not set")
    }

    @Synchronized
    fun isActiveAccountSet() = activeAccount != null

    fun getAccountKeys(accName: CyberName) = accounts[accName]

    fun addAccountKeys(accName: CyberName, keys: Set<Pair<AuthType, String>>) {
        keys.forEach {
            EosPrivateKey(it.second)
        }
        synchronized(this) {
            val oldKeys = accounts[accName.resolveAccountName()]
            val resultingKeys = keys + oldKeys.orEmpty()
            accounts[accName.resolveAccountName()] = resultingKeys
            setAccountActive(accName)
        }
    }

    @Synchronized
    fun setAccountActive(name: CyberName) {
        accounts[name.resolveAccountName()].takeIf { it == null }?.let { throw IllegalStateException("no keys for $name account name") }
        val oldUser = activeAccount
        activeAccount = name.resolveAccountName()

        val activeKey = getActiveAccountKeys().find { it.first == AuthType.ACTIVE }?.second
                ?: return
        keyListeners.forEach {
            it.onActiveKeysAdded(name.resolveAccountName(), activeKey, oldUser)
        }
    }
}