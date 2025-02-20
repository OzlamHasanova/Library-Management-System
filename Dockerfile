FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/librarymanagmentsystem.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
