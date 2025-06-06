# Pokedex API

Pokedex es una API desarrollada en Spring Boot que consume la [PokeAPI](https://pokeapi.co/api/v2) y expone endpoints para consultar información de Pokémon, incluyendo detalles, descripciones y evoluciones.

---

## Ejecución Local

```bash
export POKEMON_API=https://pokeapi.co/api/v2

export SERVER_PORT=8085

./mvnw spring-boot:run
```
o
```bash
export POKEMON_API=https://pokeapi.co/api/v2

export SERVER_PORT=8085


java -jar target/pokedex-0.0.1-SNAPSHOT.jar
```

---

##  Configuración

Asegúrate de tener configuradas las siguientes variables en tu `application.properties`:

```properties
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

##  Pruebas

get list pokemons
```
curl --location 'https://pokedex-2f23.onrender.com/api/v0/pokedex?offset=1&limit=10'
```

get pokemon by id
```
curl --location 'https://pokedex-2f23.onrender.com/api/v0/pokedex/1'
```

---

# Despliegue cloud

* Se tienen un dockerfile configurado util para  generar una imagen docker y subirla a registro publico 

* El despliegue fue en Render.com por la capa gratuita.
* Considerar que despues de un tiempo se desactica y tarda en levantar la primera vez, pero el despliegue ya esta realizado.
* Render permite conectar con el repo de github y facilita el despliegue solo considera dar de alta las variables de entorno
*  en las pruebas que se realicen siempre la primera ejecucion tardara por que no estan cacheadas las peticiones, usa cache simple, pero se podria mejora con uso de otro implementación por ejemplo usando redis.
---