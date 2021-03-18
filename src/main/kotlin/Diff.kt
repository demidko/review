import org.gitlab4j.api.models.Diff
import java.io.File
import java.io.File.createTempFile
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path

@ExperimentalPathApi
fun Diff.print(): String {

  val oldArchitecture = createTempFile(Path(oldPath).fileName.toString(), null)
  oldArchitecture.writeText(aMode.parseArchitecture())

  val newArchitecture = createTempFile(Path(newPath).fileName.toString(), null)
  newArchitecture.writeText(bMode.parseArchitecture())

  return "git diff --no-index $oldArchitecture $newArchitecture"
    .shell()
    .split("\n")
    .drop(4)
    .joinToString("\n")
}

private fun String.shell(dir: File = File(".")) = Regex("\\s")
  .let(this::split)
  .toTypedArray()
  .let(::ProcessBuilder)
  .directory(dir)
  .redirectOutput(ProcessBuilder.Redirect.PIPE)
  .redirectError(ProcessBuilder.Redirect.PIPE)
  .start()
  .apply(Process::waitFor)
  .inputStream
  .bufferedReader()
  .readText()
  .trim()
