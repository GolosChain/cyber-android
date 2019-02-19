import io.golos.commun4J.Commun4J
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.utils.Either
import io.golos.commun4J.utils.Pair
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SocialTest {

    lateinit var commun4J: Commun4J


    @Before
    fun before() {
        commun4J = Commun4J(mainTestNetConfig)
        commun4J.keyStorage.addAccountKeys(testInMainTestNetAccount.first, setOf(
                Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)
        ))
    }

    @Test
    fun setUserMeta() {
        val setMetaResult = commun4J.setUserMetadata("типа", "аппа", website = "веб-портал")
        assertTrue("meta change fail", setMetaResult is Either.Success)
        val changedMeta = (setMetaResult as Either.Success).value.processed.action_traces.first().act.data.metadata

        assertEquals("type set fail", "типа", changedMeta.type)
        assertEquals("app set fail", "аппа", changedMeta.app)
        assertEquals("web-site set fail", "веб-портал", changedMeta.website)

        val deleteResult = commun4J.deleteUserMetadata()

        assertTrue("meta delete fail", deleteResult is Either.Success)
    }

    @Test
    fun testPinUnpin() {
        val acc = AccountCreationTest.createNewAccount(mainTestNetConfig)
        val pinResult = commun4J.pin(acc.first)
        assertTrue("pin fail", pinResult is Either.Success)

        val unpinResult = commun4J.unPin(acc.first)
        assertTrue("unpin fail", unpinResult is Either.Success)
    }
}