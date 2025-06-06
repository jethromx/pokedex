package com.pokedex.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pokedex.dto.PagedResponse;
import com.pokedex.dto.ServiceResponse;
import com.pokedex.entity.PokemonInfo;
import com.pokedex.enums.Status;
import com.pokedex.exceptions.CustomException;
import com.pokedex.service.PokeApiService;
import com.pokedex.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/${api.version}/pokedex")
@Tag(name = "Pokedex", description = "Api de busqueda de Pokémon")
public class PokedexController {

     private static final Logger LOGGER = LoggerFactory.getLogger(PokedexController.class);
     private static final String LOGLINE ="PokedexController {} - {}";

    private PokeApiService pokeApiService;

    // Constructor injection for PokeApiService
    // This allows for easier testing and better separation of concerns
    // by injecting the service dependency into the controller.
    // It also makes the code cleaner and more maintainable.
    public PokedexController(PokeApiService pokeApiService) {
        this.pokeApiService = pokeApiService;
    }
    

    @GetMapping(value ="")
    @Operation(summary = "Listar pokemons", description = "Listar de todos los pokemons")
    public ResponseEntity<?> searchPokemon( 
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit) {
        
        LOGGER.info(LOGLINE, Constants.METHOD_LIST, Constants.IN);

        LOGGER.debug(LOGLINE, "searchPokemon", "offset: " + offset + ", limit: " + limit);  
        
        // Call the PokeApiService to get the list of Pokémon
       ServiceResponse<PagedResponse<PokemonInfo>> response = pokeApiService.getPokemons(offset, limit);
        
        return handleResponse(response, Constants.METHOD_LIST);
    }

    @GetMapping(value = "/{pokemonName}")
    @Operation(summary = "Obtener detalle de un pokemon", description = "Obtener detalle de un pokemon por su nombre")
    public ResponseEntity<?> getPokemonDetail(
        @PathVariable("pokemonName") String pokemonName) {
        
        LOGGER.info(LOGLINE, Constants.METHOD_GET, Constants.IN);
        LOGGER.debug(LOGLINE, "getPokemonDetail", "pokemonName: " + pokemonName);  
        
        // Call the PokeApiService to get the details of a specific Pokémon
        ServiceResponse<PokemonInfo> response = pokeApiService.getPokemonDetail(pokemonName);
        
        return handleResponse(response, Constants.METHOD_GET);
    }



    


    // Handle the response from the service
    // and return the appropriate HTTP response
     private ResponseEntity<?> handleResponse(ServiceResponse<?> response,String method) {
        LOGGER.debug(LOGLINE, "handleResponse", Constants.IN);
        //Happy path  - STATUS OK && RESPONSE OBJECT != NULL
        if (response.getStatus().equals(Status.OK) && response.getResponseObject() != null) {                   
            LOGGER.info(LOGLINE, method, Constants.OUT);
            return new ResponseEntity<>(response.getResponseObject(), response.getHttpCode());
         // Error - STATUS KO && RESPONSE OBJECT == NULL   
        } else if (response.getStatus().equals(Status.OK) && response.getResponseObject() == null) {            
            LOGGER.info(LOGLINE, method, Constants.OUT);
            return new ResponseEntity<>(buildErrorJson(response.getMessage()), response.getHttpCode());
    
        // Error - STATUS KO && RESPONSE OBJECT != NULL
        } else if (response.getStatus().equals(Status.KO) && response.getResponseObject() != null) {         
            LOGGER.error(LOGLINE, method, Constants.ERROR);
            return new ResponseEntity<>(response.getResponseObject(), response.getHttpCode());
        }
        // Error - STATUS KO && RESPONSE OBJECT == NULL
        else if (response.getStatus().equals(Status.KO) && response.getResponseObject() == null) { 
            LOGGER.error(LOGLINE, method, Constants.ERROR);
            return new ResponseEntity<>(buildErrorJson(response.getMessage()), response.getHttpCode());     
        // Error - ALL OTHER CASES
        } else { 
            LOGGER.error(LOGLINE, method, Constants.ERROR);
            throw  new CustomException("An unexpected error occurred , please try again later", "100");               
        }
    }

    // Helper para envolver el mensaje en JSON
    private Map<String, String> buildErrorJson(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("message", message);
        return error;
    }

   
}
