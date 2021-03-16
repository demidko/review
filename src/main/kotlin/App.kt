import org.gitlab4j.api.GitLabApi

fun main(args: Array<String>) {
  val gitlabUrl = args[0]
  val gitlabToken = args[1]
  val api = GitLabApi(gitlabUrl, gitlabToken).mergeRequestApi
  newWebhook(api).start(true)
}