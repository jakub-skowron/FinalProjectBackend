FROM openjdk:17-alpine
COPY /backend-0.0.1-SNAPSHOT.jar backend-0.0.1.jar

ENTRYPOINT ["java", "-jar", "backend-0.0.1.jar"]