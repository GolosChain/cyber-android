import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TransferTest {
    private val privateTestNetClient = Cyber4J(mainTestNetConfig)
    private lateinit var secondAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        privateTestNetClient.keyStorage.addAccountKeys(testInMainTestNetAccount.first,
                setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)))
        secondAccount = testInMainTestNetAccountSecond
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