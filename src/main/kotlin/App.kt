import org.gitlab.api.GitlabAPI.connect

fun main(args: Array<String>) {
  val gitlabUrl = args[0]
  val gitlabToken = args[1]
  newWebhook(connect(gitlabUrl, gitlabToken)).start(true)
}