FROM eclipse-temurin:21-jdk AS builder

WORKDIR /acme-test
COPY . .
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /acme-test

COPY --from=builder /acme-test/target/*.jar demo-acme.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","demo-acme.jar"]
