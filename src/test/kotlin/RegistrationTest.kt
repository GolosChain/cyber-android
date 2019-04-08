import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.CyberName
import org.junit.Test

class RegistrationTest {
    private val cyber4J = Cyber4J()

    @Test
    fun testGetState(){
        val state = cyber4J.getRegistrationState(CyberName("destroyer12k"), null)

        println(state)
    }
}