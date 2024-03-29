import kotlinx.coroutines.runBlocking
import org.gitlab4j.api.models.Diff
import org.gitlab4j.api.models.MergeRequest
import java.io.File
import java.lang.ProcessBuilder.Redirect.PIPE
import java.time.Duration
import java.time.Duration.between
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.timer
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.toJavaDuration

fun String.shell(dir: File = File(".")) =
  Regex("\\s")
    .let(this::split)
    .toTypedArray()
    .let(::ProcessBuilder)
    .directory(dir)
    .redirectOutput(PIPE)
    .redirectError(PIPE)
    .start()
    .apply(Process::waitFor)
    .inputStream
    .bufferedReader()
    .readText()
    .trim()

fun Diff.isJava() =
  oldPath.endsWith(".java") && newPath.endsWith(".java")

@ExperimentalTime
fun <T> cleanableListOf(lifetime: Duration = 15.minutes.toJavaDuration(), timestamp: T.() -> LocalDateTime) =
  ConcurrentLinkedQueue<T>().apply {
    timer(period = 5.minutes.toLongMilliseconds()) {
      removeAll {
        between(now(), it.timestamp()) >= lifetime
      }
    }
  }

fun <T> ConcurrentLinkedQueue<T>.tryOffer(el: T): Boolean = runBlocking {
  if (contains(el)) {
    return@runBlocking false
  }
  return@runBlocking offer(el)
}

fun MergeRequest.descriptionOrEmpty() =
  description
    .substringBefore("```diff")
    .trim()
    .takeIf(String::isNotEmpty)
    ?.plus('\n')
    .orEmpty()