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

private class MergeRequestEvent(val project: Project, val object_attributes: Attributes)
private class Attributes(val iid: Int)
private class Project(val id: Int)


@Suppress("BlockingMethodInNonBlockingContext")
fun newWebhook(api: GitlabAPI) = embeddedServer(Netty) {
  install(ContentNegotiation, Configuration::gson)
  routing {
    post("/merge_request") {
      val event = call.receive<MergeRequestEvent>()
      log.info("received {}", event)
      val mergeRequest = api.getMergeRequestChanges(event.project.id, event.object_attributes.iid)
      call.respond(OK)
    }
  }
}




