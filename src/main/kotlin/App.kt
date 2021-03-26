import org.gitlab4j.api.GitLabApi
import kotlin.io.path.ExperimentalPathApi
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalPathApi
fun main(args: Array<String>) {

  val gitlabUrl = args[0]
  val gitlabToken = args[1]

  GitLabApi(gitlabUrl, gitlabToken).let(::newWebhook).start(true)
}