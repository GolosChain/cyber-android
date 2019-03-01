import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SocialMainTestNetTest {

    lateinit var cyber4J: Cyber4J
    private lateinit var secondAcc: kotlin.Pair<CyberName, String>


    @Before
    fun before() {
        cyber4J = Cyber4J(mainTestNetConfig)
        cyber4J.keyStorage.addAccountKeys(testInMainTestNetAccount.first, setOf(
                Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)
        ))
        secondAcc = testInMainTestNetAccountSecond
    }

    @Test
    fun setUserMeta() {
        val setMetaResult = cyber4J.setUserMetadata("типа", "аппа", website = "веб-портал")
        assertTrue("meta change fail", setMetaResult is Either.Success)
        val changedMeta = (setMetaResult as Either.Success).value.processed.action_traces.first().act.data.metadata

        assertEquals("type set fail", "типа", changedMeta.type)
        assertEquals("app set fail", "аппа", changedMeta.app)
        assertEquals("web-site set fail", "веб-портал", changedMeta.website)

        val deleteResult = cyber4J.deleteUserMetadata()

        assertTrue("meta delete fail", deleteResult is Either.Success)


        val newUser = AccountCreationTest.createNewAccount(mainTestNetConfig)

        val setMetaResultSecond = cyber4J.setUserMetadata(newUser.first,
                newUser.second,
                "типа1", "аппа1", website = "веб-портал1")
        assertTrue("meta change fail", setMetaResultSecond is Either.Success)

        val changedMetaSecond = (setMetaResultSecond as Either.Success).value.extractResult().metadata


        assertEquals("type set fail", "типа1", changedMetaSecond.type)
        assertEquals("app set fail", "аппа1", changedMetaSecond.app)
        assertEquals("web-site set fail", "веб-портал1", changedMetaSecond.website)

        val deleteResultSecond = cyber4J.deleteUserMetadata(testInMainTestNetAccount.first, testInMainTestNetAccount.second)

        assertTrue("meta delete fail", deleteResultSecond is Either.Success)


    }

    @Test
    fun testPinUnpin() {
        val acc = AccountCreationTest.createNewAccount(mainTestNetConfig)
        val pinResult = cyber4J.pin(acc.first)
        assertTrue("pin fail", pinResult is Either.Success)

        val unpinResult = cyber4J.unPin(acc.first)
        assertTrue("unpin fail", unpinResult is Either.Success)

        val pinResultSecond = cyber4J.pin(secondAcc.second, secondAcc.first,
                cyber4J.keyStorage.getActiveAccount())
        assertTrue("pin fail", pinResultSecond is Either.Success)

        val unPinResultSecond = cyber4J.unPin(secondAcc.second, secondAcc.first,
                cyber4J.keyStorage.getActiveAccount())
        assertTrue("pin fail", unPinResultSecond is Either.Success)
    }

    @Test
    fun testBlocking() {
        val acc = AccountCreationTest.createNewAccount(mainTestNetConfig)
        val blockResult = cyber4J.block(acc.first)

        assertTrue("user $acc block fail", blockResult is Either.Success)

        val blockResultSecond = cyber4J.block(secondAcc.second, secondAcc.first,
                cyber4J.keyStorage.getActiveAccount())
        assertTrue("pin fail", blockResultSecond is Either.Success)


        val unblockResult = cyber4J.unBlock(acc.first)
        assertTrue("user $acc unblock fail", unblockResult is Either.Success)

        val unBlockResultSecond = cyber4J.unBlock(secondAcc.second, secondAcc.first,
                cyber4J.keyStorage.getActiveAccount())
        assertTrue("pin fail", unBlockResultSecond is Either.Success)
    }
}