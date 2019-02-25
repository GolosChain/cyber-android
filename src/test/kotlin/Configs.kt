import io.golos.commun4J.Commun4JConfig

val mainTestNetConfig = Commun4JConfig()

val privateTestNetConfig = Commun4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/", isPrivateTestNet = true)

val testInMainTestNetAccount by lazy { AccountCreationTest.createNewAccount(mainTestNetConfig) }

val testInMainTestNetAccountSecond by lazy { AccountCreationTest.createNewAccount(mainTestNetConfig) }

val testingAccountInPrivateTestNet by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

val testingAccountInPrivateTestNetSecond by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

