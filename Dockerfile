FROM openjdk:21-jdk-slim AS runtime
WORKDIR /app
RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*
ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=prod
COPY build/libs/artifact/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]