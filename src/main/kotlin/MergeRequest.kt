import org.gitlab4j.api.MergeRequestApi
import org.gitlab4j.api.models.Diff
import org.gitlab4j.api.models.MergeRequestParams
import org.slf4j.LoggerFactory.getLogger
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
fun MergeRequestApi.attachUnifiedDiff(projId: Int, mergeId: Int) {

  val data = getMergeRequestChanges(projId, mergeId)

  val diffBegin = "```diff\n"

  val diffBody = data.changes
    .joinToString(
      separator = "\n",
      transform = Diff::print
    )

  val diffEnd = "\n```"

  val description = data.description
    .substringBefore(diffBegin)
    .trim()

  val updates = MergeRequestParams()
    .withDescription("$diffBegin$diffBody$diffEnd\n$description")

  updateMergeRequest(projId, mergeId, updates)
}