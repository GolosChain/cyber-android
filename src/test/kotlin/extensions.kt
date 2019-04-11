import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.model.TransactionSuccessful

internal fun String.toCyberName() = CyberName(this)

internal fun <T> TransactionSuccessful<T>.extractResult() = this.processed.action_traces.first().act.data

internal fun generateRandomCommunName(): String {
    val builder = StringBuilder()
    (0..11).forEach {
        builder.append((Math.random() * 25).toChar() + 97)
    }
    return builder.toString()
}