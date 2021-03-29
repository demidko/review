import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.MergeRequestParams
import org.slf4j.LoggerFactory.getLogger
import java.io.File
import java.io.File.createTempFile
import java.lang.ProcessBuilder.Redirect.PIPE
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
fun GitLabApi.attachUnifiedDiff(projId: Int, mergeId: Int) {

  val log = getLogger("project $projId mr #$mergeId")


  val mergeRequest = mergeRequestApi.getMergeRequestChanges(projId, mergeId)

  val diffBegin = "```diff"

  log.info("source branch: ${mergeRequest.sourceBranch}")
  log.info("target branch: ${mergeRequest.targetBranch}")

  val diffBody = mergeRequest.changes.joinToString("\n") {


    log.info("old path: ${it.oldPath}")

    val before = when {
      it.newFile -> String()
      else -> repositoryFileApi
        .getFile(projId, it.oldPath, mergeRequest.targetBranch)
        .decodedContentAsString
        .parseArchitecture()
    }

    log.info("new path: ${it.newPath}")

    val after = when {
      it.deletedFile -> String()
      else -> repositoryFileApi
        .getFile(projId, it.newPath, mergeRequest.sourceBranch)
        .decodedContentAsString
        .parseArchitecture()
    }

    diff(before, after)
  }

  val textBody = mergeRequest.description.substringBefore(diffBegin).trim()

  val markdownDocument = "$textBody\n$diffBegin\n$diffBody\n```\n"

  log.info(markdownDocument)

  val update = MergeRequestParams().withDescription(markdownDocument)

  mergeRequestApi.updateMergeRequest(projId, mergeId, update)
}

private fun diff(before: String, after: String): String {

  val old = createTempFile("before", null).apply { writeText(before) }

  val new = createTempFile("after", null).apply { writeText(after) }

  return "git diff --no-index $old $new"
    .shell()
    .split("\n")
    .drop(5)
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