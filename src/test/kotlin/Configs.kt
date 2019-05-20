import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.CyberName
import okhttp3.logging.HttpLoggingInterceptor

val mainTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://46.4.96.246:8888/")

//val privateTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/")

val testInMainTestNetAccount by lazy {
   // Pair(CyberName("qydqfvlmccfc"), "5K8kh1pzhc5bmAwEUSvo2EZt9vBoi1bXyKiAsrPiLW5H7fYuDHp")
   //  Pair(CyberName("destroyer2k@golos"), "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ")
     AccountCreationTest.createNewAccount(mainTestNetConfig)

}

val testInMainTestNetAccountSecond by lazy {
   // Pair(CyberName("pfllqikeknim"), "5JDEt4QJNctW4pjrBViTK4PZbt5G7vVNVvGHf2SNFUWSvyrRKSM")
     AccountCreationTest.createNewAccount(mainTestNetConfig)
}

//val testingAccountInPrivateTestNet by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

//val testingAccountInPrivateTestNetSecond by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

