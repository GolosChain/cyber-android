import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.utils.Either
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SocialMainTestNetTest {

    lateinit var client: Cyber4J
    private lateinit var secondAcc: kotlin.Pair<CyberName, String>


    @Before
    fun before() {
        client = getClient()
        secondAcc = account()
    }

    @Test
    fun setUserMeta() {
        val setMetaResult = client.setUserMetadata("типа", "аппа", website = "веб-портал")
        assertTrue("meta change fail", setMetaResult is Either.Success)
        val changedMeta = (setMetaResult as Either.Success).value.processed.action_traces.first().act.data.metadata

        assertEquals("type set fail", "типа", changedMeta.type)
        assertEquals("app set fail", "аппа", changedMeta.app)
        assertEquals("web-site set fail", "веб-портал", changedMeta.website)

        val deleteResult = client.deleteUserMetadata()

        assertTrue("meta delete fail", deleteResult is Either.Success)


        val newUser = account()

        val setMetaResultSecond = client.setUserMetadata(newUser.first,
                newUser.second,
                "типа1", "аппа1", website = "веб-портал1")
        assertTrue("meta change fail", setMetaResultSecond is Either.Success)

        val changedMetaSecond = (setMetaResultSecond as Either.Success).value.extractResult().metadata


        assertEquals("type set fail", "типа1", changedMetaSecond.type)
        assertEquals("app set fail", "аппа1", changedMetaSecond.app)
        assertEquals("web-site set fail", "веб-портал1", changedMetaSecond.website)

        val deleteResultSecond = client.deleteUserMetadata(client.activeAccountPair.first, client.activeAccountPair.second)

        assertTrue("meta delete fail", deleteResultSecond is Either.Success)


    }

    @Test
    fun testPinUnpin() {
        val acc = account()
        val pinResult = client.pin(acc.first)
        assertTrue("pin fail", pinResult is Either.Success)

        val unpinResult = client.unPin(acc.first)
        assertTrue("unpin fail", unpinResult is Either.Success)

        val pinResultSecond = client.pin(secondAcc.second, secondAcc.first,
                client.keyStorage.getActiveAccount())
        assertTrue("pin fail", pinResultSecond is Either.Success)

        val unPinResultSecond = client.unPin(secondAcc.second, secondAcc.first,
                client.keyStorage.getActiveAccount())
        assertTrue("pin fail", unPinResultSecond is Either.Success)
    }

    @Test
    fun testBlocking() {
        val acc = account()
        val blockResult = client.block(acc.first)

        assertTrue("user $acc block fail", blockResult is Either.Success)

        val blockResultSecond = client.block(secondAcc.second, secondAcc.first,
                client.keyStorage.getActiveAccount())
        assertTrue("pin fail", blockResultSecond is Either.Success)


        val unblockResult = client.unBlock(acc.first)
        assertTrue("user $acc unblock fail", unblockResult is Either.Success)

        val unBlockResultSecond = client.unBlock(secondAcc.second, secondAcc.first,
                client.keyStorage.getActiveAccount())
        assertTrue("pin fail", unBlockResultSecond is Either.Success)
    }
}