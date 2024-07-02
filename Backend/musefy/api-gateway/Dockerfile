FROM openjdk:17
WORKDIR /
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]