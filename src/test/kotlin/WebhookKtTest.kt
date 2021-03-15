import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class WebhookKtTest {

  @Test
  fun testNewWebhook() {
    newWebhook().start(false)
    runBlocking {
      HttpClient().use {
        val y = it.post<String>(path = "/merge_request") {
          body = javaClass.getResourceAsStream("/MergeRequestEvent.json").bufferedReader().readText()
          contentType(ContentType.Application.Json)
        }
        println(y)
      }
    }
  }
}