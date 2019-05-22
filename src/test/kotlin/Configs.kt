import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.CyberName
import okhttp3.logging.HttpLoggingInterceptor

val mainTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://46.4.96.246:8888/")

//val privateTestNetConfig = Cyber4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/")

val testInMainTestNetAccount by lazy {
    Pair(CyberName("vvdddkepcctg"), "5HpLQBHCBR8FyXUUEX621UfSLTjLU5oqibmRyNv44TzcGW8bipS")
   //  Pair(CyberName("destroyer2k@golos"), "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ")
    // AccountCreationTest.createNewAccount(mainTestNetConfig)

}

val testInMainTestNetAccountSecond by lazy {
    Pair(CyberName("vihhrodoppgw"), "5Ke24smC8DkQ1Qnh36iu6RoKwqfsHBV5ARMFw1CM5Cbh4sUfWFU")
   //  AccountCreationTest.createNewAccount(mainTestNetConfig)
}

//val testingAccountInPrivateTestNet by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

//val testingAccountInPrivateTestNetSecond by lazy { AccountCreationTest.createNewAccount(privateTestNetConfig) }

