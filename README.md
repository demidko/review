## Review service

The service adds architectural changes diff to the merge request description.
Supports [`GitLab`](https://about.gitlab.com/)
projects with [`Java`](https://www.oracle.com/java/) language.

### Usage

You will see something similar in new merge requests description:
![ScreenShot](https://raw.githubusercontent.com/demidko/Review/main/example.jpg)

### Deploy example for [`Digital Ocean`](https://cloud.digitalocean.com/)

1. Create new user in your GitLab. Then, go to `avatar`, `settings`, `Access Tokens` and create new
   access token for him.
1. Click this button to deploy the app to the DigitalOcean App Platform.  
   [![Deploy to DigitalOcean](https://www.deploytodo.com/do-btn-blue.svg)](https://cloud.digitalocean.com/apps/new?repo=https://github.com/demidko/review/tree/main)  
   Specify `GITLAB_URL` (your gitlab host url) and `GITLAB_TOKEN` (see step 1) environment
   variables. Now, you will see your app's url.
1. Add user-bot (see step 1) to your GitLab projects as developer. In your Gitlab project, go
   to `settings`, `webhooks`, then, check the boxes like in the picture and click the green button:
   ![ScreenShot](https://raw.githubusercontent.com/demidko/Review/main/config.jpg)

