import com.google.gson.annotations.SerializedName
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.features.ContentNegotiation.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.gitlab4j.api.GitLabApi
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.io.path.ExperimentalPathApi
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
@ExperimentalPathApi
@Suppress("BlockingMethodInNonBlockingContext")
fun newWebhook(api: GitLabApi) = embeddedServer(Netty) {

  /***
   * TODO 1. list<Event(timestamp)>
   *   2. scheduler to delete old events
   */

  install(ContentNegotiation, Configuration::gson)

  install(StatusPages) {
    exception<Throwable> {
      call.respond(InternalServerError, it.stackTraceToString())
      throw it
    }
  }

  val processing = ConcurrentLinkedQueue<Event>()

  kotlin.concurrent.timer(period = 15.seconds.toLongMilliseconds()) {

  }

  routing {

    post("/merge-request") {
      val event = call.receive<Event>()
      if (event in processing) {
        return@post
      } else {

      }

      processing.offer(event)
      try {
        api.attachUnifiedDiff(event.project.id, event.mergeRequest.id)
      } catch (e: IOException) {
        log.error(e.message, e)
      }
      call.respond(OK)
    }
  }
}


private data class Event(
  @SerializedName("project") val project: Project,
  @SerializedName("object_attributes") val mergeRequest: MergeRequest
)

private data class MergeRequest(
  @SerializedName("iid") val id: Int,
  @SerializedName("last_commit") val lastCommit: LastCommit
)

private data class LastCommit(@SerializedName("id") val id: String)

private data class Project(@SerializedName("id") val id: Int)
