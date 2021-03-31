import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Event(
  @SerializedName("project") val proj: Project,
  @SerializedName("object_attributes") val mr: Attributes
) {

  data class Attributes(
    @SerializedName("iid") val id: Int,
    @SerializedName("updated_at") val updated: LocalDateTime,
    @SerializedName("last_commit") val lastCommit: Commit
  ) {
    data class Commit(
      @SerializedName("id") val id: String
    )
  }

  data class Project(
    @SerializedName("id") val id: Int
  )
}