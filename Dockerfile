# Stage 1: Build the application
FROM gradle:jdk21 AS build
WORKDIR /home/gradle/project
COPY . .
RUN ./gradlew build -x test

# Stage 2: Create the final image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
