FROM openjdk:9-slim
VOLUME /tmp
ADD /target/isa-rest-0.0.1-SNAPSHOT.jar app.jar
ADD /target/event-rest-jar-with-dependencies.jar dependencies.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","/app.jar"]