import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.MergeRequestParams
import org.slf4j.LoggerFactory.getLogger
import java.io.File.createTempFile
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
fun GitLabApi.attachUnifiedDiff(projId: Int, mergeId: Int) {

  val log = getLogger("project $projId mr #$mergeId")


  val mergeRequest = mergeRequestApi.getMergeRequestChanges(projId, mergeId)

  val diffBegin = "```diff"

  log.info("source branch: ${mergeRequest.sourceBranch}")
  log.info("target branch: ${mergeRequest.targetBranch}")

  val diffBody =
    mergeRequest
      .changes
      .filter { it.newPath.endsWith(".java") && it.oldPath.endsWith(".java") }
      .joinToString("\n") {
        val before = when {
          it.newFile -> String()
          else -> repositoryFileApi
            .getFile(projId, it.oldPath, mergeRequest.targetBranch)
            .decodedContentAsString
            .parseJavaArchitecture()
        }
        val after = when {
          it.deletedFile -> String()
          else -> repositoryFileApi
            .getFile(projId, it.newPath, mergeRequest.sourceBranch)
            .decodedContentAsString
            .parseJavaArchitecture()
        }
        diff(before, after)
      }


  val textBody = mergeRequest.description.substringBefore(diffBegin).trim()

  val markdownDocument = "$textBody\n$diffBegin\n$diffBody\n```\n"

  log.info(markdownDocument)

  val update = MergeRequestParams().withDescription(markdownDocument)

  mergeRequestApi.updateMergeRequest(projId, mergeId, update)
}



