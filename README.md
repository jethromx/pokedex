# Pokedex API

Pokedex es una API desarrollada en Spring Boot que consume la [PokeAPI](https://pokeapi.co/api/v2) y expone endpoints para consultar información de Pokémon, incluyendo detalles, descripciones y evoluciones.

---

## Ejecución Local

```bash
./mvnw spring-boot:run
```
o
```bash
java -jar target/pokedex-0.0.1-SNAPSHOT.jar
```

---

##  Configuración

Asegúrate de tener configuradas las siguientes variables en tu `application.properties`:

```properties
pokeapi.base-url=https://pokeapi.co/api/v2
spring.profiles.active=dev
```

O como variables de entorno:

```env
POKEMON_API=https://pokeapi.co/api/v2
SERVER_PORT=8085
SPRING_SWAGGER_UI_ENABLED=true
```

---

## Endpoints principales

- **GET `/api/pokemons`**  
  Lista paginada de Pokémon.

- **GET `/api/pokemons/{id|name}`**  
  Detalle de un Pokémon, mostrando:
  - Foto
  - Información básica
  - Descripción
  - Evoluciones

---

## Swagger / OpenAPI

La documentación interactiva de la API está disponible en:  
[http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)  
*(ajusta el puerto si es necesario)*

---

##  Notas

- Puedes cambiar la URL de la PokeAPI modificando `pokeapi.base-url` en `application.properties` o la variable de entorno `POKEMON_API`.
- El sistema de caché es configurable y puede adaptarse a otros proveedores (`caffeine`, `ehcache`, etc.).
- El proyecto está preparado para ser extendido y adaptado a nuevas versiones de la API o nuevas funcionalidades.

---

##  Estructura de `application.properties`

```properties
spring.application.name=pokedex
api.version=v0
pokeapi.base-url=https://pokeapi.co/api/v2
logging.level.web=INFO
spring.cache.type=simple
spring.cache.cache-names=cacheListPokemons,cacheDetailPokemon 
spring.cache.cacheListPokemons.initial-capacity=20
spring.cache.cacheListPokemons.maximum-size=100
spring.cache.cacheListPokemons.expire-after-write=1m
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
spring.profiles.active=dev
```

---


---