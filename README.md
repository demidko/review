## Review service

The service adds architectural changes diff to the merge request description.
Supports [`GitLab`](https://about.gitlab.com/)
projects with [`Java`](https://www.oracle.com/java/) language.

### Usage

You will see something similar in new merge requests description:
![ScreenShot](example.png)

### Deploy example for [`Digital Ocean`](https://cloud.digitalocean.com/)

1. Create new user in your GitLab.
1. Go to `avatar`, `settings`, `Access Tokens` and create new access token for him.
1. You need a DigitalOcean account. If you don't already have one, you
   can [`sign up`](https://cloud.digitalocean.com/registrations/new).
1. Click this button to deploy the app to the DigitalOcean App Platform.
   [![Deploy to DigitalOcean](https://www.deploytodo.com/do-btn-blue.svg)](https://cloud.digitalocean.com/apps/new?repo=https://github.com/demidko/review/tree/main)
1. Then, set GITLAB_URL (your gitlab host url) and GITLAB_TOKEN (see step 2) environment variables.
1. In your Gitlab project, go to `settings`, `webhooks`, then, check the boxes like in the picture
   and click the green button:
   ![ScreenShot](configuration.png)
1. Add user-bot to your project as developer.

