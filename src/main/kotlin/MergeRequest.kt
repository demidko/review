import org.gitlab4j.api.MergeRequestApi
import org.gitlab4j.api.models.Diff
import org.gitlab4j.api.models.MergeRequestParams
import org.slf4j.LoggerFactory.getLogger
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
fun MergeRequestApi.attachUnifiedDiff(projId: Int, mergeId: Int) {

  val data = getMergeRequestChanges(projId, mergeId)

  getLogger("changes").run {
    data.changes.forEach {
      info("before:\n${it.aMode}\nafter:\n${it.bMode}\n\n")
    }
  }

  val diffBegin = "```diff\n"

  val diffBody = data.changes
    .joinToString(
      separator = "\n",
      transform = Diff::print
    )

  val diffEnd = "\n```"

  val userDescription = data.description.substringBefore(diffBegin).trim()

  val generatedDescription = "$diffBegin$diffBody$diffEnd\n$userDescription"

  getLogger("description")
    .info(generatedDescription)

  val updates = MergeRequestParams().withDescription(generatedDescription)

  updateMergeRequest(projId, mergeId, updates)
}