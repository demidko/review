import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class RoutingKtTest {

  @Test
  fun testRouting() {
    routing().start(false)
    val event = javaClass.getResourceAsStream("/MergeRequestEvent.json").bufferedReader().readText()
    runBlocking {
      HttpClient().use {
        val y = it.post<String>(path = "/merge-request", port = 80, body = event)
        println(y)
      }
    }
  }
}