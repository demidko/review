data class MergeRequestEvent(val object_attributes: ObjectAttributes) {
  data class ObjectAttributes(val iid: Int)
}