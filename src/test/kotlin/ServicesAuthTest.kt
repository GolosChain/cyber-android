import io.golos.commun4J.Commun4J
import io.golos.commun4J.Commun4JConfig
import io.golos.commun4J.model.AuthListener
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import io.golos.commun4J.model.DiscussionTimeSort
import io.golos.commun4J.utils.Pair
import junit.framework.Assert.fail
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ServicesAuthTest {

    @Before
    fun before() {

    }

    @Test
    fun testAuth() {
        val activeUserName = CommunName("destroyer2k")
        val storage = io.golos.commun4J
                .CommunKeyStorage()
                .apply { addAccountKeys(activeUserName, setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ"))) }

        val commun4J = Commun4J(Commun4JConfig(servicesUrl = "ws://116.203.98.241:8080"), keyStorage = storage)
        commun4J.addAuthListener(object : AuthListener {
            override fun onAuthSuccess(forUser: CommunName) {
            }

            override fun onFail(e: Exception) {
                fail("auth failed")
            }
        })


        commun4J.getUserPosts("destroyer2k".toCommunName(), 20, DiscussionTimeSort.INVERTED)


        val commun4JWithoutKeys = Commun4J(Commun4JConfig(servicesUrl = "ws://116.203.98.241:8080"))

        commun4JWithoutKeys.getUserPosts("qraf".toCommunName(), 20, DiscussionTimeSort.INVERTED)

        commun4JWithoutKeys.keyStorage.addAccountKeys("destroyer2k".toCommunName(), setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ")))

        commun4JWithoutKeys.getUserPosts("asfasf".toCommunName(), 20, DiscussionTimeSort.INVERTED)

        Thread.sleep(3000)

    }
}
