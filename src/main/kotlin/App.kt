import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

data class MessageTest(val a: String, val b: Int)

fun main(args: Array<String>) {

  val token = args.firstOrNull() ?: error("You must provide a GitLab API token here")

  embeddedServer(Netty) {
    install(ContentNegotiation) {
      gson {
        setPrettyPrinting()
      }
    }
    routing {
      post("/merge-request") {
        val message = call.receive<MessageTest>()
        println(message)
      }
    }
  }.start(true)
}