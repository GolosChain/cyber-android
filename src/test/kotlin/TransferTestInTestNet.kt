import io.golos.commun4J.Commun4J
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.Pair
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TransferTestInTestNet {
    private val privateTestNetClient = Commun4J(privateTestNetConfig)
    private lateinit var secondAccount: kotlin.Pair<CommunName, String>

    @Before
    fun before() {
        privateTestNetClient.keyStorage.addAccountKeys(testingAccountInPrivateTestNet.first,
                setOf(Pair(AuthType.ACTIVE, testingAccountInPrivateTestNet.second)))
        secondAccount = testingAccountInPrivateTestNetSecond
    }

    @Test
    fun transferSomeMoney() {
        val firstTransferResult = privateTestNetClient.transfer(secondAccount.first, "0.010", "GLS")
        assertTrue("transfer fail", firstTransferResult is Either.Success)

        val seconfTransferResult = privateTestNetClient.transfer(secondAccount.second,
                secondAccount.first, privateTestNetClient.keyStorage.getActiveAccount()
                , "0.001", "GLS")
        assertTrue("transfer fail", seconfTransferResult is Either.Success)
    }
}