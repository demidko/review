import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.MergeRequestParams
import org.slf4j.LoggerFactory.getLogger
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
fun GitLabApi.attachUnifiedDiff(projId: Int, mergeId: Int) {

  val log = getLogger("project $projId mr #$mergeId")

  val mr = mergeRequestApi.getMergeRequestChanges(projId, mergeId)

  Diff(mr).use { diff ->
    diff.buildWith(repositoryFileApi)
  }

  val diffBegin = "```diff"


  val textBody = mr.description.substringBefore(diffBegin).trim()

  val markdownDocument = "$textBody\n$diffBegin\n$diffBody\n```\n"

  log.info(markdownDocument)

  val update = MergeRequestParams().withDescription(markdownDocument)

  mergeRequestApi.updateMergeRequest(projId, mergeId, update)
}



