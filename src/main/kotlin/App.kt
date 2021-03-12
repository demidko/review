import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {

  val token = args.first()

  embeddedServer(Netty) {
    routing {
      post("/merge-request") {

      }
    }
  }.start(true)
}