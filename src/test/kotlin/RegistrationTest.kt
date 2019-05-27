import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.utils.AuthUtils
import io.golos.cyber4j.utils.Either
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.util.*

class RegistrationTest {
    private val client = getClient()
    val unExistingPhone = generatePhone()
    val pass = (Cyber4J::class.java).getResource("/phonekey.txt").readText()

    @Test
    fun testGetState() {
        val state = client.getRegistrationState(null, "+773217337584")

        assertTrue(state is Either.Success)
    }

    @Test
    fun testAccCreationThroughGate() {
        val accName = generateRandomCommunName()

        val firstStepSuccess = client.firstUserRegistrationStep("any12", unExistingPhone, pass)

        assertTrue(firstStepSuccess is Either.Success)

        println(firstStepSuccess)


        val secondStep = client.verifyPhoneForUserRegistration(unExistingPhone, (firstStepSuccess as Either.Success).value.code)

        assertTrue(secondStep is Either.Success)

        println(secondStep)

        val thirdStep = client.setVerifiedUserName(accName.toCyberName(), unExistingPhone)

        assertTrue(thirdStep is Either.Success)

        println(thirdStep)

        val keys = AuthUtils.generatePublicWiFs(accName, generatePass(), AuthType.values())

        val lastStep = client.writeUserToBlockChain(accName.toCyberName(), keys[AuthType.OWNER]!!,
                keys[AuthType.ACTIVE]!!,
                keys[AuthType.POSTING]!!,
                keys[AuthType.MEMO]!!)

        assertTrue(lastStep is Either.Success)

        println(lastStep)

    }
}

fun generatePhone(): String {
    val sb = StringBuilder("+7")
    (0..10).forEach {
        sb.append((Math.random() * 10).toInt())
    }
    return sb.toString()
}

fun generatePass() = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "")