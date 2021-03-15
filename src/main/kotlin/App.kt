fun main(args: Array<String>) {
  val token = args.firstOrNull() ?: error("You must provide a GitLab API token")
  newWebhook().start(true)
}