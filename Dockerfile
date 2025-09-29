# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app

# Copy everything (backend + frontend)
COPY . .

# Build the jar (skip tests to speed up CI/CD builds)
RUN mvn clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/target/yamb-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Start the app
CMD ["java", "-jar", "app.jar"]
