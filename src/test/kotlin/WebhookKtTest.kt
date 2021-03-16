import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.OK
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class WebhookKtTest {

  @Test
  fun testNewWebhook() {

    val event = javaClass
      .getResourceAsStream("/MergeRequestEvent.json")
      .bufferedReader()
      .readText();

    newWebhook("").start(false)

    runBlocking {
      HttpClient().use { client ->
        client
          .post<HttpStatusCode>(path = "/merge_request", body = event) {
            contentType(Json)
          }
          .let {
            assertThat(it, equalTo(OK))
          }
      }
    }
  }
}