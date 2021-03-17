import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class ParserKtTest {

  @Test
  fun parseJavaArchitecture() = assertThat(

    javaClass.getResourceAsStream("/Test.java")
      .bufferedReader()
      .readText()
      .parseJavaArchitecture(),

    // language=java
    equalTo(
      """
      class Clazz {
        public void action();
        public void actionWithParams(int x, int y);
        public int val1 = 6;
      }
      interface ITest {
        void foo();
      }
      """.trimIndent()
    )
  )

}