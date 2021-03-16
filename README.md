## Code review service

The service adds architectural changes diff to the merge request description.

### Usage

### Build with [`Gradle`](https://gradle.org/)

* Execute command `gradle clean test shadow` to build self-executable jar.

Then you can start the application with the `java -jar *.jar [host_url] [token]` command.

### Or, deploy to cloud with [`Digital Ocean`](https://cloud.digitalocean.com/)

* Select repository [`here`](https://cloud.digitalocean.com/apps) to start microservice.

Then you can customize application arguments via `HOST_URL` and `TOKEN` Environment Variables.
