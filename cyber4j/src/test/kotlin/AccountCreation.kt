import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.AuthType
import CyberName
import io.golos.cyber4j.utils.AuthUtils
import io.golos.cyber4j.utils.Either
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.charset.Charset
import java.util.*


class AccountCreationTest {


    @Test
    fun testAccountCreation() {
        val client = getClient()
        val pass = UUID.randomUUID().toString()
        val newUser = generateRandomCommunName()
        val accCreationResult = client.createAccount(newUser, pass, eosCreateKey)

        assertTrue("account creation failure on main net for user $newUser", accCreationResult is Either.Success)

    }

    @Test
    fun createAccountAndPrintIt() {
        val client = getClient()
        val pass = UUID.randomUUID().toString()
        val newUser = generateRandomCommunName()
        val activeKey = AuthUtils.generatePrivateWiFs(newUser, pass, arrayOf(AuthType.ACTIVE))[AuthType.ACTIVE]!!

        val accCreationResult = client.createAccount(newUser, pass, eosCreateKey)

        assertTrue("account creation failure on main net for user $newUser", accCreationResult is Either.Success)

        print("name = $newUser activeKey = $activeKey")

    }

    companion object {
        private val eosCreateKey = (Cyber4J::class.java).getResource("/eoscreateacckey.txt").readText(Charset.defaultCharset())


        fun createNewAccount(forConfig: Cyber4JConfig): Pair<CyberName, String> {
            val client = io.golos.cyber4j.Cyber4J(forConfig)
            val pass = UUID.randomUUID().toString()
            val newUser = generateRandomCommunName()

            client.createAccount(newUser, pass, eosCreateKey) as Either.Success

            return Pair(CyberName(newUser), AuthUtils.generatePrivateWiFs(newUser, pass, arrayOf(AuthType.ACTIVE))[AuthType.ACTIVE]!!)
        }
    }
}