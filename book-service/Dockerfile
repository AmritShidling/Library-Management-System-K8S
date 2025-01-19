FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/book-service-2.0.0.jar /app/book-service-2.0.0.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "book-service-2.0.0.jar"]
