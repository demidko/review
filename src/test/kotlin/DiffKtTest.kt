import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
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

    assertThat(diff.toHumanView(), equalTo(
      """
        @@ -1,8 +1,10 @@
         class Clazz {
        -  public void action();
        -  public void actionWithParams(int x, int y);
        +  public void action2();
           public int val1 = 6;
         }
         interface ITest {
           void foo();
         }
        +interface ITest2 {
        +  int foo();
        +}
      """.trimIndent()
    ))
  }

}


