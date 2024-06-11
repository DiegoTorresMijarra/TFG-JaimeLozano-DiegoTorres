# Etapa de compilación
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -X

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine AS run
WORKDIR /app
COPY --from=build /app/target/*.jar /app/my-app.jar
EXPOSE 3000
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app/my-app.jar"]
