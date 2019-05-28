import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.Either
import java.io.File
import java.util.concurrent.ConcurrentHashMap

enum class CONFIG_TYPE {
    STABLE, DEV, UNSTABLE
}

private val unstableConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://116.202.4.46:8888/",
        servicesUrl = "wss://159.69.33.136:8080")

private val devConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://46.4.96.246:8888/",
        servicesUrl = "ws://159.69.33.136:8080")

private val stableConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://116.202.4.39:8888/",
        servicesUrl = "wss://116.203.98.241:8080")

private fun CONFIG_TYPE.toConfig() = when (this) {
    CONFIG_TYPE.STABLE -> stableConfig
    CONFIG_TYPE.DEV -> devConfig
    CONFIG_TYPE.UNSTABLE -> unstableConfig
}

private fun CyberName.checkAccount(forConfig: CONFIG_TYPE): Boolean {
    val acc = Cyber4J(forConfig.toConfig()).getUserAccount(this)
    return acc is Either.Success
}

private val delimeter = "###"

private fun getAccount(sourceFile: File,
                       configType: CONFIG_TYPE): Pair<CyberName, String> {
    val out: Pair<CyberName, String>

    out = if (!sourceFile.exists()) {
        AccountCreationTest.createNewAccount(configType.toConfig())
    } else {
        val contents = sourceFile.readText().split(delimeter)

        if (contents.isEmpty() || contents.size != 2) AccountCreationTest.createNewAccount(configType.toConfig())

        val cyberName = CyberName(contents[0])
        val key = contents[1]
        if (!cyberName.checkAccount(configType)) {
            AccountCreationTest.createNewAccount(configType.toConfig())
        } else cyberName to key
    }
    sourceFile.writeText("${out.first.name}$delimeter${out.second}")
    return out
}

private fun firstAccount(forConfig: CONFIG_TYPE): Pair<CyberName, String> {
    return getAccount(File(File(".").canonicalPath, "/first_acc_$forConfig.txt"), forConfig)
    //  Pair(CyberName("destroyer2k@golos"), "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ")
}

private val savedAccs = ConcurrentHashMap<CONFIG_TYPE, Pair<CyberName, String>>()

fun account(forConfig: CONFIG_TYPE = CONFIG_TYPE.DEV): Pair<CyberName, String> {
    return savedAccs.getOrPut(forConfig) {
        getAccount(File(File(".").canonicalPath,
                "/second_acc_$forConfig.txt"), forConfig)
    }
}

@Synchronized
fun getClient(ofType: CONFIG_TYPE = CONFIG_TYPE.DEV): Cyber4J {
    return Cyber4J(config = ofType.toConfig())
            .apply {
                val account = firstAccount(ofType)
                keyStorage.addAccountKeys(account.first,
                        setOf(io.golos.cyber4j.utils.Pair(AuthType.ACTIVE, account.second)))
            }
}


