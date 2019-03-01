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
        val commun = io.golos.cyber4j.Cyber4J(mainTestNetConfig)
        val pass = UUID.randomUUID().toString()
        val newUser = generateRandomCommunName()
        val accReationResult = commun.createAccount(newUser, pass, eosCreateKey)

        assertTrue("account creation failure on main net for user $newUser", accReationResult is Either.Success)
    }

    @Test
    fun testAccountCreationOnTestNet() {
        val commun = io.golos.cyber4j.Cyber4J(privateTestNetConfig)
        val pass = UUID.randomUUID().toString()
        val newUser = generateRandomCommunName()
        val accReationResult = commun.createAccount(newUser, pass, eosCreateKey)

        assertTrue("account creation failure on testnet for user $newUser", accReationResult is Either.Success)
    }

    @Test
    fun generateUser(){
        println(createNewAccount(privateTestNetConfig))
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