# syntax = docker/dockerfile:experimental
FROM gradle:jdk15 as builder
WORKDIR /project
COPY src ./src
COPY build.gradle.kts ./build.gradle.kts
RUN --mount=type=cache,target=./.gradle gradle clean test shadowJar

FROM openjdk:15 as backend
WORKDIR /root
COPY --from=builder /project/*.jar ./app
ENV GITLAB_URL=gitlab.example.com
ENV GITLAB_TOKEN=example_secret_token
ENTRYPOINT "java" "-jar" "/root/app" $GITLAB_URL $GITLAB_TOKEN
