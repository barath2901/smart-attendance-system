
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the Application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# COPY the built jar from the previous stage
COPY --from=build /app/target/*.jar app.jar
# Expose port 8080 (Standard for Spring Boot)
EXPOSE 8080
# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]