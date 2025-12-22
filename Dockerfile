# Official OpenJDK 17 image from docket hub
FROM eclipse-temurin:17-jre
# Set working directory inside the container
WORKDIR /app
# Copy the compiled java application JAR file into the container
COPY target/userservice-0.0.1-SNAPSHOT.jar app.jar
# Expose the port the spring boot application will run on
EXPOSE 8080
# Command to run the application
CMD ["java", "-jar", "app.jar"]