FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

ENV GRADLE_OPTS="-Dorg.gradle.internal.http.connectionTimeout=60000 -Dorg.gradle.internal.http.socketTimeout=60000"

COPY . .

ARG MODULE_NAME

RUN chmod +x gradlew
RUN ./gradlew :${MODULE_NAME}:clean :${MODULE_NAME}:bootJar -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

ARG MODULE_NAME
COPY --from=builder /app/backend/${MODULE_NAME}/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]