import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.OK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.gitlab4j.api.GitLabApi
import org.junit.jupiter.api.Test
import kotlin.io.path.ExperimentalPathApi

class WebhookKtTest {

  @ExperimentalPathApi
  @Test
  fun newWebhookTest() {

    mockk<GitLabApi>(relaxed = true)
      .let(::newWebhook)
      .start(false)

    javaClass.getResourceAsStream("/MergeRequestEvent.json")
      .bufferedReader()
      .readText()
      .let(this::postJson)
      .let {
        assertThat(it, equalTo(OK))
      }
  }

  private fun postJson(json: String) = runBlocking {
    HttpClient().use {
      it.post<HttpStatusCode>(path = "merge-request", body = json) {
        contentType(Json)
      }
    }
  }
}