import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import io.golos.commun4J.utils.AuthUtils
import io.golos.commun4J.utils.Either
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.charset.Charset
import java.util.*


class AccountCreationTest {


    @Test
    fun testAccountCreationOnMainnet() {
        val commun = io.golos.commun4J.Commun4J(mainTestNetConfig)
        val pass = UUID.randomUUID().toString()
        val newUser = generateRandomCommunName()
        val accReationResult = commun.createAccount(newUser, pass, eosCreateKey)

        assertTrue("account creation failure on main net for user $newUser", accReationResult is Either.Success)
    }

    @Test
    fun testAccountCreationOnTestNet() {
        val commun = io.golos.commun4J.Commun4J(privateTestNetConfig)
        val pass = UUID.randomUUID().toString()
        val newUser = generateRandomCommunName()
        val accReationResult = commun.createAccount(newUser, pass, eosCreateKey)

        assertTrue("account creation failure on testnet for user $newUser", accReationResult is Either.Success)
    }


    companion object {
        private val eosCreateKey = (AuthUtils::class.java).getResource("/eoscreateacckey.txt").readText(Charset.defaultCharset())

        fun createNewAccount(forConfig: Commun4JConfig = mainTestNetConfig): Pair<CommunName, String> {
            val commun = io.golos.commun4J.Commun4J(forConfig)
            val pass = UUID.randomUUID().toString()
            val newUser = generateRandomCommunName()

            commun.createAccount(newUser, pass, eosCreateKey) as Either.Success

            return Pair(CommunName(newUser), AuthUtils.generatePrivateWiFs(newUser, pass, arrayOf(AuthType.ACTIVE))[AuthType.ACTIVE]!!)

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