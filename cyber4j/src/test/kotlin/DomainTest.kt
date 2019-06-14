import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.utils.Either
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.nio.charset.Charset
import java.util.*

class DomainTest {
    private val client = getClient()

    @Test
    fun setUserName() {
        val glsCreatorKey = (Cyber4J::class.java).getResource("/eoscreateacckey.txt").readText(Charset.defaultCharset())

        val newAcc = account(client.config.toConfigType())
        val result = client.newUserName("gls".toCyberName(),
                newAcc.first,
                UUID.randomUUID().toString().replace("-", ""),
                glsCreatorKey)

        assertTrue(result is Either.Success)
    }
}