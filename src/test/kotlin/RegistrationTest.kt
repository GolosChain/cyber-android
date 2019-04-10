import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.Either
import junit.framework.Assert.assertTrue
import org.junit.Test

class RegistrationTest {
    private val cyber4J = Cyber4J()
    val unExistingPhone = "+70000000211"
    val pass = (Cyber4J::class.java).getResource("/phonekey.txt").readText()

    @Test
    fun testGetState() {
        val state = cyber4J.getRegistrationState(CyberName("phaza"), null)

        assertTrue(state is Either.Success)
    }

    @Test
    fun testAccCreationThroughGate() {
        val accName = "phaza21"
        val state = cyber4J.getRegistrationState(CyberName(accName), null)
        val resp = cyber4J.firstUserRegistrationStep("any12", unExistingPhone, pass)

        println(resp)
    }
}