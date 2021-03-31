import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class Event(
  @SerializedName("project") val proj: Project,
  @SerializedName("object_attributes") val mr: Attributes
) {

  val received: LocalDateTime = now()

  data class Attributes(
    @SerializedName("iid") val id: Int,
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