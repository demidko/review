import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.System.setOut

class AppKtTest {

  @Test
  fun testMain() = runBlocking {


    /*GlobalScope.launch {
      main(arrayOf("test-api-token"))
    }

    val x = HttpClient().use { client ->
      client.get<String>("http://127.0.0.1/")
    }*/

    println("xdf")


  }
}