import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun routing() = embeddedServer(Netty) {
  install(ContentNegotiation) {
    gson { setPrettyPrinting() }
  }
  routing {
    post("/merge-request") {
      val iid = call.receive<MergeRequestEvent>()
      call.respond(iid.object_attributes.iid)
    }
    get("/") { call.respondText("ok") }
  }
}