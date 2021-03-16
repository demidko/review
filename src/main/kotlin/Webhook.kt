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
import org.gitlab.api.GitlabAPI

private class Event(val object_attributes: Attributes)
private class Attributes(val iid: Int)

fun newWebhook(api: GitlabAPI) = embeddedServer(Netty) {
  install(ContentNegotiation, Configuration::gson)
  routing {
    post("/merge_request") {
      val iid = call.receive<Event>().object_attributes.iid
      log.info("received merge request iid {}", iid)

      call.respond(OK)
    }
  }
}




