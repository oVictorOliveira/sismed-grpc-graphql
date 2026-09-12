FROM maven:3.9.9-eclipse-temurin-21 AS build

RUN mkdir -p /app
COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine-3.23

RUN mkdir /app

COPY --from=build /app/target/sismed-scheduling-service-*.jar sismed-scheduling-service.jar

WORKDIR /app

RUN echo "America/Sao_Paulo" > /etc/timezone

ENTRYPOINT ["java","-jar","/sismed-scheduling-service.jar"]