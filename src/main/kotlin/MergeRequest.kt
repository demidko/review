import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.MergeRequestParams
import org.slf4j.LoggerFactory.getLogger
import java.io.File
import java.io.File.createTempFile
import java.lang.ProcessBuilder.Redirect.PIPE
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
fun GitLabApi.attachUnifiedDiff(projId: Int, mergeId: Int) {

  val mergeRequest = mergeRequestApi.getMergeRequestChanges(projId, mergeId)

  val diffBegin = "```diff"

  val diffBody = mergeRequest.changes.joinToString("\n") {

    val before = when (it.oldPath) {
      null -> ""
      else -> repositoryFileApi
        .getFile(projId, it.oldPath, mergeRequest.targetBranch)
        .decodedContentAsString
        .parseArchitecture()
    }

    val after = when (it.newPath) {
      null -> ""
      else -> repositoryFileApi
        .getFile(projId, it.newPath, mergeRequest.sourceBranch)
        .decodedContentAsString
        .parseArchitecture()
    }

    diff(before, after)
  }

  val text = mergeRequest.description.substringBefore(diffBegin).trim()

  val description = "$text$diffBegin\n$diffBody\n```\n"

  getLogger("description").info(description)

  val update = MergeRequestParams().withDescription(description)

  mergeRequestApi.updateMergeRequest(projId, mergeId, update)
}

private fun diff(before: String, after: String): String {

  val old = createTempFile("before", null).apply { writeText(before) }

  val new = createTempFile("after", null).apply { writeText(after) }

  return "git diff --no-index $old $new"
    .shell()
    .split("\n")
    .drop(4)
    .joinToString("\n")
}

private fun String.shell(dir: File = File(".")) =
  Regex("\\s")
    .let(this::split)
    .toTypedArray()
    .let(::ProcessBuilder)
    .directory(dir)
    .redirectOutput(PIPE)
    .redirectError(PIPE)
    .start()
    .apply(Process::waitFor)
    .inputStream
    .bufferedReader()
    .readText()
    .trim()