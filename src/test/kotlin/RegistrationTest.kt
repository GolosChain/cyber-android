import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.utils.AuthUtils
import io.golos.cyber4j.utils.Either
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.util.*

class RegistrationTest {
    private val cyber4J = Cyber4J()
    val unExistingPhone = "+70000000219"
    val pass = (Cyber4J::class.java).getResource("/phonekey.txt").readText()

    @Test
    fun testGetState() {
        val state = cyber4J.getRegistrationState("hpwkuvaktvmn".toCyberName(), null)
        println(generatePass())

        assertTrue(state is Either.Success)
    }

    @Test
    fun testAccCreationThroughGate() {
        val accName = generateRandomCommunName()

        val firstStepSuccess = cyber4J.firstUserRegistrationStep("any12", unExistingPhone, pass)

        assertTrue(firstStepSuccess is Either.Success)

        println(firstStepSuccess)


        val secondStep = cyber4J.verifyPhoneForUserRegistration(unExistingPhone, (firstStepSuccess as Either.Success).value.code)

        assertTrue(secondStep is Either.Success)

        println(secondStep)

        val thirdStep = cyber4J.setVerifiedUserName(accName.toCyberName(), unExistingPhone)

        assertTrue(thirdStep is Either.Success)

        println(thirdStep)

        val keys = AuthUtils.generatePublicWiFs(accName, generatePass(), AuthType.values())

        val lastStep = cyber4J.writeUserToBlockChain(accName.toCyberName(), keys[AuthType.OWNER]!!,
                keys[AuthType.ACTIVE]!!,
                keys[AuthType.POSTING]!!,
                keys[AuthType.MEMO]!!)

        assertTrue(lastStep is Either.Success)

        println(lastStep)

    }
}

fun generatePass() = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "")