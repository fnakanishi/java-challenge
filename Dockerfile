FROM maven:3.9.1-eclipse-temurin-17-alpine AS build

COPY . /app

WORKDIR /app

RUN mvn install

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/basico-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]

CMD ["-jar", "app.jar"]