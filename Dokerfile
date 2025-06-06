# Dockerfile para Spring Boot Pokedex API 

# Imagen base oficial de OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el jar generado por Maven/Gradle al contenedor
COPY target/pokedex-*.jar app.jar

# Expone el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]