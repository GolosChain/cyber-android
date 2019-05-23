import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.Either
import java.io.File

val mainTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://46.4.96.246:8888/")

//val privateTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/")

private val cyber4J: Cyber4J = Cyber4J()

private fun CyberName.checkAccount(): Boolean {
    val acc = cyber4J.getUserAccount(this)
    return acc is Either.Success
}

private val delimeter = "###"

private fun accountForFile(file: File): Pair<CyberName, String> {
    val out: Pair<CyberName, String>

    out = if (!file.exists()) {
        AccountCreationTest.createNewAccount(mainTestNetConfig)
    } else {
        val contents = file.readText().split(delimeter)

        if (contents.isEmpty() || contents.size != 2) AccountCreationTest.createNewAccount(mainTestNetConfig)

        val cyberName = CyberName(contents[0])
        val key = contents[1]
        if (!cyberName.checkAccount()) {
            AccountCreationTest.createNewAccount(mainTestNetConfig)
        } else cyberName to key
    }
    file.writeText("${out.first.name}$delimeter${out.second}")
    return out
}

val testInMainTestNetAccount: Pair<CyberName, String> by lazy {
    accountForFile(File(File(".").canonicalPath, "/first_acc.txt"))
    //  Pair(CyberName("destroyer2k@golos"), "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ")

}


val testInMainTestNetAccountSecond: Pair<CyberName, String> by lazy {
    accountForFile(File(File(".").canonicalPath, "/second_acc.txt"))
}

//val testingAccountInPrivateTestNet by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

//val testingAccountInPrivateTestNetSecond by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

