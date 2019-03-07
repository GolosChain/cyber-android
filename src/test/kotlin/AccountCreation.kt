import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.AuthUtils
import io.golos.cyber4j.utils.Either
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.charset.Charset
import java.util.*


class AccountCreationTest {


    @Test
    fun testAccountCreationOnMainnet() {
        val client = io.golos.cyber4j.Cyber4J(mainTestNetConfig)
        val pass = UUID.randomUUID().toString()
        val newUser = generateRandomCommunName()
        val accCreationResult = client.createAccount(newUser, pass, eosCreateKey)

        assertTrue("account creation failure on main net for user $newUser", accCreationResult is Either.Success)

    }

    @Test
    fun testAccreation() {
        val client = io.golos.cyber4j.Cyber4J(Cyber4JConfig(blockChainHttpApiUrl = "http://46.4.96.246:8888/"))
        val pass = UUID.randomUUID().toString()
        val newUser = generateRandomCommunName()
        val accCreationResult = client.createAccount(newUser, pass, eosCreateKey)
        val activeKey = AuthUtils.generatePrivateWiFs(newUser, pass, arrayOf(AuthType.ACTIVE))[AuthType.ACTIVE]!!

//        assertTrue("account creation failure on main net for user $newUser", accCreationResult is Either.Success)
//
//        val result = client.openTokenBalance(newUser.toCyberName(), eosCreateKey)
//        println((result as Either.Success).value.extractResult())
//
//        val issuseResult = client.issueTokens(newUser.toCyberName(), eosCreateKey, "3.000 GLS")
//
//        issuseResult as Either.Success
//
//        val transferToIssuer = client.transfer(activeKey, newUser.toCyberName(), "gls.vesting".toCyberName(), "0.100", "GLS")
//
//        transferToIssuer as Either.Success

        val transferResult = client.transfer(activeKey, newUser.toCyberName(), "destroyer2k".toCyberName(), "0.010", "GLS")

        transferResult as Either.Success

    }


    companion object {
        private val eosCreateKey = (AuthUtils::class.java).getResource("/eoscreateacckey.txt").readText(Charset.defaultCharset())

        fun createNewAccount(forConfig: Cyber4JConfig = mainTestNetConfig): Pair<CyberName, String> {
            val commun = io.golos.cyber4j.Cyber4J(forConfig)
            val pass = UUID.randomUUID().toString()
            val newUser = generateRandomCommunName()

            commun.createAccount(newUser, pass, eosCreateKey) as Either.Success

            return Pair(CyberName(newUser), AuthUtils.generatePrivateWiFs(newUser, pass, arrayOf(AuthType.ACTIVE))[AuthType.ACTIVE]!!)
        }

        private fun generateRandomCommunName(): String {
            val builder = StringBuilder()
            (0..11).forEach {
                builder.append((Math.random() * 25).toChar() + 97)
            }
            return builder.toString()
        }
    }
}