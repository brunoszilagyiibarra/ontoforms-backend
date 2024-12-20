FROM amazoncorretto:17-alpine
ADD build/libs/ontology-form-generator-api-0.0.1-SNAPSHOT.jar ontology-form-generator-api-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ontology-form-generator-api-0.0.1-SNAPSHOT.jar"]