FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY build/libs/employee-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
