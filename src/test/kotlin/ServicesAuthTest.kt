import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.Cyber4JConfig
import io.golos.cyber4j.model.*
import io.golos.cyber4j.utils.Pair
import junit.framework.Assert.fail
import org.junit.Before
import org.junit.Test

class ServicesAuthTest {

    @Before
    fun before() {

    }

    @Test
    fun testAuth() {
        val activeUserName = CyberName("destroyer2k")
        val storage = io.golos.cyber4j
                .KeyStorage()
                .apply { addAccountKeys(activeUserName, setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ"))) }

        val commun4J = Cyber4J(Cyber4JConfig(servicesUrl = "ws://116.203.98.241:8080"), keyStorage = storage)
        commun4J.addAuthListener(object : AuthListener {
            override fun onAuthSuccess(forUser: CyberName) {
            }

            override fun onFail(e: Exception) {
                fail("auth failed")
            }
        })


        commun4J.getUserPosts("destroyer2k".toCyberName(), ContentParsingType.WEB, 20, DiscussionTimeSort.INVERTED)


        val commun4JWithoutKeys = Cyber4J(Cyber4JConfig(servicesUrl = "ws://116.203.98.241:8080"))

        commun4JWithoutKeys.getUserPosts("qraf".toCyberName(), ContentParsingType.WEB, 20, DiscussionTimeSort.INVERTED)

        commun4JWithoutKeys.keyStorage.addAccountKeys("destroyer2k".toCyberName(), setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ")))

        commun4JWithoutKeys.getUserPosts("asfasf".toCyberName(), ContentParsingType.WEB, 20, DiscussionTimeSort.INVERTED)

        Thread.sleep(3000)

    }

    @Test
    fun testAuth2() {
        val commun4JWithoutKeys = Cyber4J(Cyber4JConfig(servicesUrl = "ws://116.203.98.241:8080"))
        commun4JWithoutKeys.addAuthListener(object : AuthListener {
            override fun onAuthSuccess(forUser: CyberName) {
            }

            override fun onFail(e: Exception) {
                println("onFail $e")
                fail("auth failed")
            }
        })
        commun4JWithoutKeys.keyStorage.addAccountKeys("destroyer2k".toCyberName(), setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ")))
        commun4JWithoutKeys.getUserPosts("asfasf".toCyberName(), ContentParsingType.WEB, 20, DiscussionTimeSort.INVERTED)

        Thread.sleep(3000)
    }
}
