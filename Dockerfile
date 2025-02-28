FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/Library-Managment-System-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
