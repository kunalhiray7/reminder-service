FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD /build/libs/reminder-service-0.0.1-SNAPSHOT.jar reminder-service.jar
ENTRYPOINT ["java", "-jar", "reminder-service.jar"]