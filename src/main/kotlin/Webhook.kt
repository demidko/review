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
import org.gitlab4j.api.models.Diff
import kotlin.io.path.ExperimentalPathApi


@ExperimentalPathApi
@Suppress("BlockingMethodInNonBlockingContext")
fun newWebhook(api: MergeRequestApi) = embeddedServer(Netty) {
  install(ContentNegotiation, Configuration::gson)
  routing {
    post("/") {
      val (proj, mr) = call.receive<Event>()
      val data = api.getMergeRequestChanges(proj.id, mr.id)


      val diff = data.changes
        .filterNot(Diff::getDeletedFile)
        .joinToString("\n", transform = Diff::toHumanView)

      call.respond(OK)
    }
  }
}

private data class Event(
  @SerializedName("project") val project: Project,
  @SerializedName("object_attributes") val mergeRequest: MergeRequest
)

private class MergeRequest(
  @SerializedName("iid") val id: Int
)

private class Project(
  @SerializedName("id") val id: Int
)





