import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.model.AuthType
import io.golos.cyber4j.model.Tag
import io.golos.cyber4j.utils.Either
import io.golos.cyber4j.utils.Pair
import junit.framework.Assert.assertTrue
import org.junit.Test


class PostInPublicTestNet {
    private var client = Cyber4J(mainTestNetConfig).apply {
        keyStorage.addAccountKeys(testInMainTestNetAccount.first,
                setOf(Pair(AuthType.ACTIVE, testInMainTestNetAccount.second)))
    }

    @Test
    fun testCreatePost() {
        val result = client.createPost("яяя яяя ЫА  ЫВа SDF23523 twe etwtewtew wjnetnwetm kwm205ioi295290923дтыпл ыулпь лыпдвьплыь лпыжвт лытжывь пыжд ьпыдж ьпдыж ьджыь джыь жыпь джыь жыпь жS",
                "sgsggsdgsgd",
                listOf(Tag("test")))

        assertTrue(result is Either.Success)
    }
}