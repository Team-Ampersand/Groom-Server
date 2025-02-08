FROM openjdk:21-jdk-slim AS runtime
WORKDIR /app
RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*
ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=prod
COPY groom-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]