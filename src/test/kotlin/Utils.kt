import io.golos.cyber4j.model.CyberName
import junit.framework.Assert.fail
import org.junit.Test

class Utils {
    @Test
    fun cyberNameTest() {
        CyberName("destroyer2k")
        CyberName("destroyer2k@golos")

        try {
            CyberName("des#sftroyer2k")
            fail()
        } catch (e: Exception) {

        }
    }


}