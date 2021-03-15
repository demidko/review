import io.ktor.application.*
import io.ktor.features.*
import io.ktor.features.ContentNegotiation.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun newWebhook() = embeddedServer(Netty) {
  install(ContentNegotiation, Configuration::gson)
  routing {
    post("/merge_request") {
      val iid = call.receive<Event>().object_attributes.iid

      call.respondText(iid.toString())
    }
  }
}



private class Event(val object_attributes: ObjectAttributes)

private class ObjectAttributes(val iid: Int)
