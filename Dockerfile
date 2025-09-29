# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# If you need submodules, replicate .profile behavior here:
RUN git submodule update --init --recursive || true

# Build the jar
RUN mvn clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/yamb-0.0.1-SNAPSHOT.jar app.jar

# App port (adjust if needed)
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
