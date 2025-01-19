FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/borrow-service-2.0.0.jar /app/borrow-service-2.0.0.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "borrow-service-2.0.0.jar"]
