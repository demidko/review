import org.gitlab4j.api.RepositoryFileApi
import org.gitlab4j.api.models.Diff
import org.gitlab4j.api.models.MergeRequest
import java.io.File



fun Diff.architectureDiff(api: RepositoryFileApi, mergeRequest: MergeRequest) {

  val before = when {
    newFile -> String()
    else -> api
      .getFile(projId, oldPath, mergeRequest.targetBranch)
      .decodedContentAsString
      .parseJavaArchitecture()
  }
  val after = when {
    deletedFile -> String()
    else -> api
      .getFile(projId, it.newPath, mergeRequest.sourceBranch)
      .decodedContentAsString
      .parseJavaArchitecture()
  }
  diff(before, after)
}

fun diff(before: String, after: String): String {

  val old = File.createTempFile("before", null).apply { writeText(before) }

  val new = File.createTempFile("after", null).apply { writeText(after) }

  return "git diff --no-index $old $new"
    .shell()
    .split("\n")
    .drop(5)
    .joinToString("\n")
}