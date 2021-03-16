import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.OK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class WebhookKtTest {

  @Test
  fun testNewWebhook() {

    newWebhook(mockk(relaxed = true)).start(false)

    val event = javaClass
      .getResourceAsStream("/MergeRequestEvent.json")
      .bufferedReader()
      .readText()
      .let(this::sendMergeRequestEvent)

    assertThat(event, equalTo(OK))
  }

  private fun sendMergeRequestEvent(event: String) = runBlocking {
    HttpClient().use {
      it.post<HttpStatusCode>(path = "/merge_request", body = event) {
        contentType(Json)
      }
    }
  }
}