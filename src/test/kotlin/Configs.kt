import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.CyberName

val mainTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://46.4.96.246:8888/")

//val privateTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/")

val testInMainTestNetAccount by lazy {
    Pair(CyberName("hvujxlinjqde"), "5JGkegd2EZogsX4t4kARd1ufLZQ6gR8wgNum9yL4tSjuKfRaYJp")
   // Pair(CyberName("destroyer2k@golos"), "5HvAHpoDu4zhm53FXiPFCdoVJYtTEATrdCrHG9zD4Umg1BHEHyT")
   // AccountCreationTest.createNewAccount(mainTestNetConfig)

}

val testInMainTestNetAccountSecond by lazy {
    Pair(CyberName("ohqasprgksdn"), "5KASdJHiokLBD4F4727jKnkuem35GVFCPW6QeFj1rTTv5AYZohB")
            // AccountCreationTest.createNewAccount(mainTestNetConfig)
}

//val testingAccountInPrivateTestNet by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

//val testingAccountInPrivateTestNetSecond by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

