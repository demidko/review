import com.google.gson.JsonDeserializer
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
import org.gitlab4j.api.GitLabApi
import java.time.LocalDateTime
import java.time.ZonedDateTime.parse
import java.time.format.DateTimeFormatter.ofPattern
import kotlin.io.path.ExperimentalPathApi
import kotlin.time.ExperimentalTime


@ExperimentalTime
@ExperimentalPathApi
@Suppress("BlockingMethodInNonBlockingContext")
fun newWebhook(api: GitLabApi) = embeddedServer(Netty) {

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

  val processing = cleanableListOf<Event> { mr.updated }

  routing {
    post("/merge-request") {
      call.receive<Event>()
        .takeIf(processing::tryOffer)
        ?.apply {
          log.info("processing: $this")
          api.attachUnifiedDiff(proj.id, mr.id)
        }
      call.respond(OK)
    }
  }
}
