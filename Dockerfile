# Build stage
FROM maven:3.9.6-amazoncorretto-21 AS build

COPY src /home/app/src

COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Package stage
FROM amazoncorretto:21

COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar

EXPOSE 80

ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]