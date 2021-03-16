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
    newWebhook("").start(false)
    runBlocking {
      HttpClient().use { client ->
        client
          .post<HttpStatusCode>(path = "/merge_request") {
            contentType(Json)
            body = javaClass.getResourceAsStream("/MergeRequestEvent.json").bufferedReader().readText()
          }
          .let { status ->
            assertThat(status, equalTo(OK))
          }
      }
    }
  }
}