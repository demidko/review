import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.MergeRequestParams
import java.time.LocalDateTime.now
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
fun GitLabApi.attachUnifiedDiff(projId: Int, mergeId: Int) {

  val mr = mergeRequestApi.getMergeRequestChanges(projId, mergeId)

  val update =
    MergeRequestParams()
      .withDescription(
        """
        Description generated at ${now()} by review service
        ${mr.description()}
        ```diff
        ${diff(mr, repositoryFileApi)}
        ```
        """.trimIndent()
      )
  mergeRequestApi.updateMergeRequest(projId, mergeId, update)
}



