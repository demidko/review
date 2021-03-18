import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.gitlab4j.api.models.Diff
import org.junit.jupiter.api.Test
import kotlin.io.path.ExperimentalPathApi


class DiffKtTest {

  @ExperimentalPathApi
  @Test
  fun printTest() {

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

    assertThat(diff.print(), equalTo(
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


