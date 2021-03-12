## Code review service

The service adds architectural changes diff to the merge request description.

### Run with [`Docker`](https://www.docker.com/products/docker-desktop)

* Execute command `API_TOKEN=[your api token here] docker-compose up` to start the application in a
  container.

### Build with [`Gradle`](https://gradle.org/)

* Execute command `gradle clean test shadow` to build self-executable jar.

Then you can start the application with the `java -jar *.jar [your api token here]` command.
