import io.ktor.application.*
import io.ktor.features.*
import io.ktor.features.ContentNegotiation.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun newWebhook() = embeddedServer(Netty) {
  install(ContentNegotiation, Configuration::gson)
  routing {
    post("/merge_request") {
      val iid = call.receiveMergeRequestIid()

    }
  }
}

suspend fun ApplicationCall.receiveMergeRequestIid(): Int {
  class ObjectAttributes(val iid: Int)
  class Event(val object_attributes: ObjectAttributes)
  return receive<Event>().object_attributes.iid
}


