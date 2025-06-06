# Dockerfile para Spring Boot Pokedex API 

# Etapa 1: Compilación
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# Imagen base oficial de OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el jar generado por Maven/Gradle al contenedor
COPY --from=build /app/target/pokedex-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]