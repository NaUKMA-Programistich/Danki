FROM openjdk:17-jdk-slim

ADD ./build/libs/backend.jar backend.jar
ADD ./build/resources/main/dictionary-data ./src/main/resources/dictionary-data

ENTRYPOINT ["java", "-jar","backend.jar"]

EXPOSE 8080