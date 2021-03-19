import com.google.gson.annotations.SerializedName
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.features.ContentNegotiation.*
import io.ktor.gson.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.gitlab4j.api.MergeRequestApi
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
@Suppress("BlockingMethodInNonBlockingContext")
fun newWebhook(api: MergeRequestApi) = embeddedServer(Netty) {
  install(ContentNegotiation, Configuration::gson)
  val processing = ConcurrentLinkedQueue<Event>()
  routing {
    post("/merge-request") {

      val event = call.receive<Event>()
      if (event in processing) {
        return@post
      }
      processing.offer(event)

      try {
        api.attachUnifiedDiff(event.project.id, event.mergeRequest.id)
      } catch (e: IOException) {
        log.error(e.message, e)
      } finally {
        processing.remove(event)
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
  @SerializedName("iid") val id: Int
)

private data class Project(
  @SerializedName("id") val id: Int
)
