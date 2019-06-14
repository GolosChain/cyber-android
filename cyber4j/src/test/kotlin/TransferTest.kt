import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.Either
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TransferTest {
    private lateinit var client: Cyber4J
    private lateinit var secondAccount: kotlin.Pair<CyberName, String>

    @Before
    fun before() {
        client = getClient()
        secondAccount = account(client.config.toConfigType())
    }

    @Test
    fun transferSomeMoney() {
        val firstTransferResult = client.transfer(secondAccount.first, "0.010", "GOLOS")
        assertTrue("transfer fail", firstTransferResult is Either.Success)

        val seconfTransferResult = client.transfer(secondAccount.second,
                secondAccount.first, client.keyStorage.getActiveAccount()
                , "0.001", "GOLOS")
        assertTrue("transfer fail", seconfTransferResult is Either.Success)
    }
}