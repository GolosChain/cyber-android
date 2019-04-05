import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import junit.framework.Assert.assertTrue
import org.junit.Test

class WitnessVoteTest {
    @Test
    fun testVoteAndUnvote() {
        val cyber4j = Cyber4J(mainTestNetConfig)

        //name = wmhbptpodfeu activeKey = 5J9bWrWFPxvLDbViBYh32k9Ei7aT8afyuWiwzkNzGHhtoZXmjtU
        cyber4j.keyStorage.addAccountKeys("wmhbptpodfeu".toCyberName(), setOf(Pair(AuthType.ACTIVE, "5J9bWrWFPxvLDbViBYh32k9Ei7aT8afyuWiwzkNzGHhtoZXmjtU")))

        val createWitnessResult = cyber4j.registerAWitness("url")

        assertTrue(createWitnessResult is Either.Success)

        val voteResult = cyber4j.voteForAWitness("wmhbptpodfeu".toCyberName(), 0.toShort())

        assertTrue(voteResult is Either.Success)

        val unvoteResult = cyber4j.unVoteForAWitness("wmhbptpodfeu".toCyberName())

        assertTrue(unvoteResult is Either.Success)

        val unregisterWitnessResult = cyber4j.unRegisterWitness()

        assertTrue(unregisterWitnessResult is Either.Success)

        println(voteResult)
    }
}