import org.gitlab4j.api.RepositoryFileApi
import org.gitlab4j.api.models.Diff
import org.gitlab4j.api.models.MergeRequest
import java.nio.file.Files.createTempDirectory
import kotlin.io.path.*

@ExperimentalPathApi
fun diff(mr: MergeRequest, api: RepositoryFileApi): String {

  val sourceTmpDirectory = createTempDirectory("${mr.sourceBranch}${mr.sourceProjectId}")

  val targetTmpDirectory = createTempDirectory("${mr.targetBranch}${mr.targetProjectId}")

  try {
    return mr
      .changes
      .filter(Diff::isJava)
      .joinToString("\n") { diff ->

        val oldFile =
          (targetTmpDirectory / Path(diff.oldPath).name)
            .createFile()
            .apply {
              if (!diff.newFile) {
                api
                  .getRawFile(mr.targetProjectId, mr.targetBranch, diff.oldPath)
                  .bufferedReader()
                  .readText()
                  .parseJavaArchitecture()
                  .let(::writeText)
              }
            }

        val newFile =
          (sourceTmpDirectory / Path(diff.newPath).name)
            .createFile()
            .apply {
              if (!diff.deletedFile) {
                api
                  .getRawFile(mr.sourceProjectId, mr.sourceBranch, diff.newPath)
                  .bufferedReader()
                  .readText()
                  .parseJavaArchitecture()
                  .let(::writeText)
              }
            }

        "git diff --no-index $oldFile $newFile"
          .shell()
          .split("\n")
          .drop(3)
          .joinToString("\n")
          .trim()
      }
  } finally {
    sourceTmpDirectory.toFile().deleteRecursively()
    targetTmpDirectory.toFile().deleteRecursively()
  }
}