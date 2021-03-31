import org.gitlab4j.api.GitLabApiException
import org.gitlab4j.api.RepositoryFileApi
import org.gitlab4j.api.models.Diff
import org.gitlab4j.api.models.MergeRequest
import java.io.File.createTempFile
import java.nio.file.Files.createTempDirectory
import kotlin.io.path.ExperimentalPathApi


@ExperimentalPathApi
fun Diff.architectureDiff(api: RepositoryFileApi, mergeRequest: MergeRequest) {

  val sourceDirectory = createTempDirectory("id${mergeRequest.sourceProjectId}_${mergeRequest.sourceBranch}")

  val targetDirectory = createTempDirectory("id${mergeRequest.targetProjectId}_${mergeRequest.targetBranch}")

  val oldFile = try {
    api.getRawFile(mergeRequest.targetProjectId, mergeRequest.targetBranch, oldPath, targetDirectory.toFile())
  } catch (ignored: GitLabApiException) {
    createTempFile("", null)
  }

  val newFile = try {
    api.getRawFile(mergeRequest.sourceProjectId, mergeRequest.sourceBranch, newPath, sourceDirectory.toFile())
  } catch (ignored: GitLabApiException) {
    createTempFile("", null)
  }


  val diff = "git diff --no-index $oldFile $newFile".shell().split("\n").joinToString("\n")


  a
  newFile.delete()
  oldFile.delete()


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