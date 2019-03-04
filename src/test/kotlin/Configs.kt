import io.golos.cyber4j.Cyber4JConfig

val mainTestNetConfig = Cyber4JConfig()

//val privateTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/")

val testInMainTestNetAccount by lazy { AccountCreationTest.createNewAccount(mainTestNetConfig) }

val testInMainTestNetAccountSecond by lazy { AccountCreationTest.createNewAccount(mainTestNetConfig) }

//val testingAccountInPrivateTestNet by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

//val testingAccountInPrivateTestNetSecond by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

