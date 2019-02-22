import io.golos.commun4J.model.CommunName
import io.golos.commun4J.model.TransactionSuccessful

internal fun String.toCommunName() = CommunName(this)

internal fun <T> TransactionSuccessful<T>.extractResult() = this.processed.action_traces.first().act.data