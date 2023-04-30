FROM openjdk:17-alpine

COPY target/filmcatalog-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "filmcatalog-0.0.1-SNAPSHOT.jar"]