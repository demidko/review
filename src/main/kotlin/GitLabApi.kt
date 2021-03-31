import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.MergeRequestParams
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
fun GitLabApi.attachUnifiedDiff(projId: Int, mergeId: Int) {

  val mr = mergeRequestApi.getMergeRequestChanges(projId, mergeId)

  val update =
    MergeRequestParams()
      .withDescription(
        "${mr.descriptionOrEmpty()}```diff\n${diff(mr, repositoryFileApi)}\n```"
      )

  mergeRequestApi.updateMergeRequest(projId, mergeId, update)
}



