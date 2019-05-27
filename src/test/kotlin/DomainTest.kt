import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.utils.Either
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.nio.charset.Charset

class DomainTest {
    private val client = getClient()

    @Test
    fun setUserName() {
        val glsCreatorKey = (Cyber4J::class.java).getResource("/glscreatorkey.txt").readText(Charset.defaultCharset())

        val newAcc = account()
        val result = client.newUserName("la12mb32la31".toCyberName(),
                newAcc.first, "super-username2", glsCreatorKey)

        assertTrue(result is Either.Success)
    }
}