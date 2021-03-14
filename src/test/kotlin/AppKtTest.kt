import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class AppKtTest {

  @Test
  fun testMain() {

    GlobalScope.launch {
      main(arrayOf(""))
    }

    runBlocking {
      delay(7000)
      HttpClient().use { client ->
        val x = client.get<String>("http://127.0.0.1/")
        println(x)
      }
    }
  }
}