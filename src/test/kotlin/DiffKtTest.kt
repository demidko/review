import org.gitlab4j.api.models.Diff
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.ProcessBuilder.Redirect.PIPE
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path


class DiffKtTest {

  @ExperimentalPathApi
  @Test
  fun diffTest() {

    val diff = Diff().apply {
      oldPath = "/AVersion.java"
      newPath = "/BVersion.java"
      aMode = javaClass.getResourceAsStream(oldPath)
        .bufferedReader()
        .readText()
      bMode = javaClass.getResourceAsStream(newPath)
        .bufferedReader()
        .readText()
    }

    println(diff.toHumanView() + "\n")
  }

}


