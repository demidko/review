import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import org.gitlab4j.api.GitLabApi
import java.time.Duration.between
import java.time.LocalDateTime
import java.time.ZonedDateTime.parse
import java.time.format.DateTimeFormatter.ofPattern
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.timer
import kotlin.io.path.ExperimentalPathApi
import kotlin.time.ExperimentalTime
import kotlin.time.hours

private data class Event(
  @SerializedName("project") val project: Project,
  @SerializedName("object_attributes") val mergeRequest: MergeRequest
)

private data class MergeRequest(
  @SerializedName("iid") val id: Int,
  @SerializedName("updated_at") val updated: LocalDateTime
)

private data class Project(
  @SerializedName("id") val id: Int
)

@ExperimentalTime
@ExperimentalPathApi
@Suppress("BlockingMethodInNonBlockingContext")
fun newWebhook(api: GitLabApi) = embeddedServer(Netty) {

  val processing = ConcurrentLinkedQueue<Event>()

  timer(period = 1.hours.toLongMilliseconds(), name = "events cleaner") {
    processing.removeAll {
      val lifetime = between(LocalDateTime.now(), it.mergeRequest.updated)
      lifetime.toHours() >= 1
    }
  }

  install(ContentNegotiation) {
    gson {
      registerTypeAdapter(
        LocalDateTime::class.java,
        JsonDeserializer { json, _, _ ->
          parse(json.asJsonPrimitive.asString, ofPattern("yyyy-MM-dd HH:mm:ss z"))
            .toLocalDateTime()
        }
      )
    }
  }

  install(StatusPages) {
    exception<Throwable> {
      call.respond(InternalServerError, it.stackTraceToString())
      throw it
    }
  }

  routing {
    post("/merge-request") {
      val event = call.receive<Event>()
      if (processing.tryOffer(event)) {
        api.attachUnifiedDiff(event.project.id, event.mergeRequest.id)
      }
      call.respond(OK)
    }
  }
}

fun <T> ConcurrentLinkedQueue<T>.tryOffer(el: T): Boolean = runBlocking {
  if (contains(el)) {
    return@runBlocking false
  }
  return@runBlocking offer(el)
}


