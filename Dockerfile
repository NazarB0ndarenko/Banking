FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /home/app
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /home/app
COPY --from=build /home/app/target/*.jar banking.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "banking.jar"]

LABEL authors="Nazar"
