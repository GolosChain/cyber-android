import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.CyberName

val mainTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/")

//val privateTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/")

val testInMainTestNetAccount by lazy {
    //Pair(CyberName("vhgandicbrlh"), "5JPkQvkCrM3FU7DwejdYtWFboPaWb29TTCdas8YCUSM4z7m9HAP")
    AccountCreationTest.createNewAccount(mainTestNetConfig)

}

val testInMainTestNetAccountSecond by lazy {
    //Pair(CyberName("ehiqjgkceowk"), "5KASdJHiokLBD4F4727jKnkuem35GVFCPW6QeFj1rTTv5AYZohB")
    AccountCreationTest.createNewAccount(mainTestNetConfig)
}

//val testingAccountInPrivateTestNet by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

//val testingAccountInPrivateTestNetSecond by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

