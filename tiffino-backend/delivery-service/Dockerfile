FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/delivery-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8970
ENTRYPOINT ["java", "-jar", "app.jar"]
