# ------------ Stage 1: Build JAR using Maven ------------
ARG MAVEN_IMAGE=maven:3.9.9-ibm-semeru-17-focal
FROM ${MAVEN_IMAGE} AS build

# Set working directory for build
WORKDIR /vm-build

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# ------------ Stage 2: Run JAR using minimal runtime image ------------
FROM gcr.io/distroless/java17-debian11

# Create app directory
WORKDIR /vm

# Copy JAR from build stage
COPY --from=build /vm-build/target/vamika-*.jar /vm/vm-microservice.jar

# Expose application port
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "vm-microservice.jar"]