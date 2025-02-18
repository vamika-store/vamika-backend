ARG BUILDER_IMAGE=maven:3.9.9-ibm-semeru-17-focal
FROM ${BUILDER_IMAGE}

# Set the working directory
WORKDIR /vm-build

# Copy source code
COPY . .

# Build the code
RUN mvn clean \
    && mvn -DskipTests=true package \
    && mkdir -p /vm \
    && mv /vm-build/target/vamika-*.jar /vm/vm-microservice.jar \
    && rm -rf /vm-build/*

# Set the working directory
WORKDIR /vm

# Set the entry point to run the jar

CMD ["java", "-jar", "vm-microservice.jar"]
