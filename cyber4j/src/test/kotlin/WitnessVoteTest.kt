import io.golos.cyber4j.utils.Either
import junit.framework.Assert.assertTrue
import org.junit.Test

class WitnessVoteTest {
    @Test
    fun testVoteAndUnvote() {
        val client = getClient()

        val newAccount = AccountCreationTest.createNewAccount(client.config)
        client.setActiveAccount(newAccount)

        val createWitnessResult = client.registerAWitness("url")

        assertTrue(createWitnessResult is Either.Success)

        val voteResult = client.voteForAWitness(client.activeAccountPair.first)

        assertTrue(voteResult is Either.Success)

        val unvoteResult = client.unVoteForAWitness(client.activeAccountPair.first)

        assertTrue(unvoteResult is Either.Success)

        Thread.sleep(1_000)

        val unregisterWitnessResult = client.unRegisterWitness()

        assertTrue(unregisterWitnessResult is Either.Success)

    }
}