# Stage 1: Build the application with JDK 19
FROM openjdk:19-ea-11-jdk-oracle AS build
WORKDIR /app

# Install Maven by downloading the binary distribution and extracting it
RUN curl -o maven.tar.gz https://archive.apache.org/dist/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz && \
    tar -xzf maven.tar.gz -C /opt/ && \
    ln -s /opt/apache-maven-3.8.4/bin/mvn /usr/local/bin/mvn && \
    rm -f maven.tar.gzd

# Copy the pom.xml and source code into the Docker image
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Stage 2: Create the final Docker image using JDK 19 for running the application
FROM openjdk:19-ea-11-jdk-oracle
WORKDIR /app

# Copy the JAR file from the build stage into the final image
COPY --from=build /app/target/EnrollingService-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port the app runs on
EXPOSE 8084

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
