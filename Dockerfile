# Builder stage of the spring boot app
FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

COPY . .

# Give permissions to the gradlew script
RUN chmod +x ./gradlew

# Create the jar file
RUN ./gradlew build

# Final stage of the spring boot app
FROM openjdk:21-jdk-slim AS production

LABEL authors="growtech"

ARG APP_PORT=8080

WORKDIR /app

# Copy the built jar file from the builder stage
COPY --from=builder /app/build/libs/growtech-api-0.0.1-SNAPSHOT.jar ./api-sensores.jar

# Copy the entrypoint script and give it execution rights
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE ${APP_PORT}

# Check if the environment variables are set
ENTRYPOINT ["/entrypoint.sh"]

# Run the application
CMD ["java", "-jar", "/app/api-sensores.jar"]