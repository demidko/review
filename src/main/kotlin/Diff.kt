import com.github.difflib.DiffUtils.diff
import com.github.difflib.UnifiedDiffUtils.generateUnifiedDiff
import org.gitlab4j.api.models.Diff
import java.io.File



fun Diff.toHumanViewOld(): String {
  val oldArchitecture = aMode.architecture().split("\n")
  val newArchitecture = bMode.architecture().split("\n")
  val patch = diff(oldArchitecture, newArchitecture)
  val difference = generateUnifiedDiff(oldPath, newPath, oldArchitecture, patch, 0)
  return difference.joinToString("\n")
}
