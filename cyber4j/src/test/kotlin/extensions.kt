import io.golos.cyber4j.Cyber4J
import io.golos.cyber4j.KeyStorage
import io.golos.cyber4j.model.AuthType
import io.golos.sharedmodel.CyberName
import io.golos.sharedmodel.Either

internal fun String.toCyberName() = CyberName(this)

fun generateRandomCommunName(): String {
    val builder = StringBuilder()
    (0..11).forEach {
        builder.append((Math.random() * 25).toChar() + 97)
    }
    return builder.toString()
}

val KeyStorage.activeAccountPair: Pair<CyberName, String>
    get() = Pair(getActiveAccount(), this.getActiveAccountKeys().find { it.first == AuthType.ACTIVE }!!.second)

val Cyber4J.activeAccountPair
    get() = keyStorage.activeAccountPair

fun <S, F> Either<S, F>.getOrThrow(): S = (this as Either.Success).value

fun Cyber4J.setActiveAccount(pair: Pair<CyberName, String>) {
    keyStorage.addAccountKeys(pair.first,
            setOf(io.golos.cyber4j.utils.Pair(AuthType.ACTIVE, pair.second)))
}