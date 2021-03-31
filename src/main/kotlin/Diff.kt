import org.gitlab4j.api.RepositoryFileApi
import org.gitlab4j.api.models.Diff
import org.gitlab4j.api.models.MergeRequest
import java.io.File.createTempFile
import java.nio.file.Files.createTempDirectory
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.nameWithoutExtension


@ExperimentalPathApi
fun Diff.architectureDiff(api: RepositoryFileApi, mergeRequest: MergeRequest) {

  val sourceDirectory = createTempDirectory(
    "${mergeRequest.sourceProjectId}_${mergeRequest.sourceBranch}"
  )

  val targetDirectory = createTempDirectory(
    "${mergeRequest.targetProjectId}_${mergeRequest.targetBranch}"
  )

  val oldName = Path(oldPath).nameWithoutExtension

  val newName = Path(newPath).nameWithoutExtension

  sourceDirectory / "df"

  val oldContent = when {
    newFile -> String()
    else -> api
      .getFile(mergeRequest.projectId, oldPath, mergeRequest.targetBranch)
      .decodedContentAsString
      .parseJavaArchitecture()
  }

  val newContent = when {
    deletedFile -> String()
    else -> api
      .getFile(mergeRequest.projectId, newPath, mergeRequest.sourceBranch)
      .decodedContentAsString
      .parseJavaArchitecture()
  }



  diff(oldContent, newContent)
}

fun diff(beforeName: String, beforeContent: String, afterName: String, afterContent: String): String {

  val old = createTempFile(beforeName, ".java").apply { writeText(beforeContent) }

  val new = createTempFile(afterName, ".java").apply { writeText(afterContent) }

  return "git diff --no-index $old $new"
    .shell()
    .split("\n")
    .joinToString("\n")
}