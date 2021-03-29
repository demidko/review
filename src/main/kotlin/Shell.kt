import java.io.File

fun String.shell(dir: File = File(".")) =
  Regex("\\s")
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