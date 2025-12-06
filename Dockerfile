# ---------- Stage 1: Build the Application ----------
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy only pom.xml first (helps use Docker cache)
COPY pom.xml .

# Copy entire project
COPY src ./src

# Build the jar (skip tests for faster deployment)
RUN mvn -B -DskipTests clean package


# ---------- Stage 2: Run the Application ----------
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy JAR file from previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
