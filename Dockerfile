FROM gradle:8.12.1-jdk21 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN chmod +x gradlew
RUN ./gradlew build
FROM openjdk:21-jdk-slim AS runtime
WORKDIR /app
RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*
ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=prod
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]