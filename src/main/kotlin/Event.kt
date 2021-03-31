import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Event(
  @SerializedName("project") val proj: Project,
  @SerializedName("object_attributes") val mr: Attributes
) {

  data class Attributes(
    @SerializedName("iid") val id: Int,
    @SerializedName("updated_at") val updated: LocalDateTime
  )

  data class Project(
    @SerializedName("id") val id: Int
  )
}