package com.pokedex.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.pokedex.config.AppConfig;
import com.pokedex.dto.PagedResponse;
import com.pokedex.dto.PokemonDto;
import com.pokedex.dto.PokemonEvolutionChainDto;
import com.pokedex.dto.PokemonItemDto;
import com.pokedex.dto.PokemonListDto;
import com.pokedex.dto.PokemonSpeciesDto;
import com.pokedex.dto.ServiceResponse;
import com.pokedex.entity.Chain;
import com.pokedex.entity.EggGroup;
import com.pokedex.entity.PokemonInfo;
import com.pokedex.enums.Status;
import com.pokedex.service.PokeApiService;
import com.pokedex.util.Constants;

@Service
public class PokeApiServiceImpl implements PokeApiService {

    private static final String LOGLINE = "PokeApiServiceImpl {} - {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(PokeApiServiceImpl.class);

    private final AppConfig appConfig;  
    private final RestTemplate restTemplate; 
    public String BASE_URL;

    PokeApiServiceImpl(AppConfig appConfig) {        
        this.appConfig = appConfig;
        this.BASE_URL = this.appConfig.getBaseUrl();
        this.restTemplate = new RestTemplate();
    }

    @Cacheable(value = Constants.CACHE_LIST_POKEMONS, key = "#offset + '-' + #limit")
    public ServiceResponse<PagedResponse<PokemonInfo>> getPokemons(int offset, int limit) {
        LOGGER.info(LOGLINE, Constants.METHOD_LIST, Constants.IN);

        String url = BASE_URL + "/pokemon/?offset=" + offset + "&limit=" + limit;
        PokemonListDto listResponse = null;

        // Fetch the list of Pokémon from the PokeAPI
        // Using RestTemplate to make a GET request to the PokeAPI
        // The response is mapped to the PokemonListResponse class
        // This class contains the count, next, previous, and results fields
        try {
            // Make the GET request to the PokeAPI
            LOGGER.info(LOGLINE, "Calling API", Constants.IN);
            listResponse = restTemplate.getForObject(url, PokemonListDto.class);
            LOGGER.info(LOGLINE, "Calling API", Constants.OUT);
        } catch (RestClientException ex) {
            LOGGER.error("Error al consumir la API de Pokemons: {}", ex.getMessage());
            return this.buildErrorResponse("Error al consultar la API de Pokemons", HttpStatus.SERVICE_UNAVAILABLE);
        }

        // Validación unificada para respuesta nula o sin resultados
        if (listResponse == null || listResponse.getResults() == null || listResponse.getResults().isEmpty()) {
            LOGGER.error(LOGLINE, "pokemon list empty o null ", Constants.OUT);
            return this.buildErrorResponse("No Pokémon found", HttpStatus.NO_CONTENT);
        }

        // Map the list of Pokémon items to a list of PokemonInfo objects
        // Each item in the results is a PokemonListItem, which contains the name and
        // URL of the Pokémon
        // The mapToPokemonInfo method is used to convert each PokemonListItem to a
        // PokemonInfo object
        // The filter ensures that only non-null PokemonInfo objects are included in the
        // final list

        List<PokemonInfo> pokemonInfo = listResponse.getResults()
                .stream() // if you want to use parallel processing, you can uncomment the next line and
                          //  commented this line
                // .parallelStream() // Uncomment this line to enable parallel processing, API
                // POKEMON maybe rate-limited o blocked
                .map(this::safeMapToPokemonInfo)
                .filter(Objects::nonNull)
                .toList();

        // Check if the list of Pokémon information is empty
        if (pokemonInfo.isEmpty()) {
            LOGGER.debug(LOGLINE, "Pokemon list is empty", Constants.OUT);
            return this.buildErrorResponse("No Pokémon found", HttpStatus.NO_CONTENT);
            
        }

        PagedResponse<PokemonInfo> paged = new PagedResponse<>(
                pokemonInfo,
                offset,
                limit,
                pokemonInfo.size()// total number of Pokémon in the current page

        );

        LOGGER.info(LOGLINE, Constants.METHOD_LIST, Constants.OUT);
        // Return the list of Pokémon information wrapped in a ServiceResponse object
        return new ServiceResponse<>(Status.OK, paged, "Pokémon list retrieved successfully", HttpStatus.OK);

    }

    @Cacheable(value = Constants.CACHE_DETAIL_POKEMON)
    public ServiceResponse<PokemonInfo> getPokemonDetail(String nameOrId) {
        String url = BASE_URL + "/pokemon/" + nameOrId;
        LOGGER.info(LOGLINE, "getPokemonDetail", Constants.IN);

        // 1. Basic details
        PokemonDto detail;
        try {
            detail = restTemplate.getForObject(url, PokemonDto.class);
        }
        catch (RestClientException ex) {
            LOGGER.error("Error al consumir la API de Pokemons: {}", ex.getMessage());            
            return this.buildErrorResponse("Error al consultar la API de Pokemons", HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (detail == null) {
            LOGGER.info(LOGLINE, "getPokemonDetail", Constants.OUT);
            return this.buildErrorResponse("No Pokémon found", HttpStatus.NOT_FOUND);
        }

        PokemonInfo info = mapPokemonDtoToInfo(detail);

        // 2. details
      
        String speciesUrl = BASE_URL + "/pokemon-species/" + nameOrId;
        PokemonSpeciesDto species  = null;

         try {
            species = restTemplate.getForObject(speciesUrl, PokemonSpeciesDto.class);
            }
        catch (RestClientException ex) {
            LOGGER.error("Error al consumir la API de Pokemons: {}", ex.getMessage());
            return this.buildErrorResponse("Error al consultar la API de Pokemons", HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (species != null ) {
            info.setBaseHapinnes(species.getBase_happiness());
            info.setCaptureRate(species.getCapture_rate());
            info.setEggGroups(
                    species.getEgg_groups() != null
                            ? species.getEgg_groups().stream()
                                    .map(EggGroup::getName)
                                    .toList()
                            : new ArrayList<>());
            info.setColor(species.getColor() != null ? species.getColor().getName() : null);

            if(species.getEvolution_chain() != null) {
                // Set the evolution chain URL if it exists
                PokemonEvolutionChainDto pokemonEvolutionChainDto = restTemplate.getForObject(species.getEvolution_chain().getUrl(), PokemonEvolutionChainDto.class);
                if (pokemonEvolutionChainDto != null && pokemonEvolutionChainDto.getChain() != null) {
                    info.setEvolutions(extractEvolutions(pokemonEvolutionChainDto.getChain()));
                }
            }
            
        }

 
        LOGGER.info(LOGLINE, "getPokemonDetail", Constants.OUT);
        return new ServiceResponse<>(Status.OK, info, "Pokémon detail retrieved successfully", HttpStatus.OK);
    }





    // Mapeo de DTO a Info
    private PokemonInfo mapPokemonDtoToInfo(PokemonDto detail) {
        PokemonInfo info = new PokemonInfo();
        info.setId(detail.getId());
        info.setName(detail.getName());
        info.setWeight(detail.getWeight());
        info.setPhotoUrl(detail.getSprites() != null ? detail.getSprites().getFront_default() : null);
        info.setType(detail.getTypes() != null && !detail.getTypes().isEmpty()
                ? detail.getTypes().get(0).getType().getName()
                : null);
        info.setAbilities(detail.getAbilities() != null
                ? detail.getAbilities().stream().map(a -> a.getAbility().getName()).toList()
                : new ArrayList<>());
        return info;
    }



    // Método auxiliar para extraer evoluciones
    private List<String> extractEvolutions(Chain chain) {
        LOGGER.info(LOGLINE, "extractEvolutions", Constants.IN);
        List<String> evolutions = new ArrayList<>();
        Chain current = chain;
        while (current != null) {
            evolutions.add(current.getSpecies().getName());
            if (current.getEvolves_to() != null && !current.getEvolves_to().isEmpty()) {
                current = current.getEvolves_to().get(0);
            } else {
                break;
            }
        }
        LOGGER.info(LOGLINE, "extractEvolutions", Constants.OUT);
        return evolutions;
    }


    // Manejo seguro de mapToPokemonInfo
    private PokemonInfo safeMapToPokemonInfo(PokemonItemDto item) {
        try {
            return mapToPokemonInfo(item);
        } catch (RestClientException ex) {
            LOGGER.warn("Error al obtener detalle de Pokémon {}: {}", item != null ? item.getName() : "null", ex.getMessage());
            return null;
        }
    }


    private PokemonInfo mapToPokemonInfo(PokemonItemDto item) {

        LOGGER.debug(LOGLINE, "mapToPokemonInfo", Constants.IN);

        if (item == null || item.getUrl() == null || item.getUrl().isEmpty()) {
            LOGGER.debug(LOGLINE, "mapToPokemonInfo", "Item or URL is null or empty");
            return null;
        }
        // Fetch detailed information for each Pokémon using its URL
        // The URL is obtained from the PokemonListItem object
        // The detailed information is mapped to the PokemonDto class
        // This class contains fields such as name, weight, sprites, types, and
        // abilities
        PokemonDto detail = restTemplate.getForObject(item.getUrl(), PokemonDto.class);
        if (detail == null)
            return null;

        PokemonInfo info = new PokemonInfo();
        info.setId(detail.getId());
        info.setName(detail.getName());
        info.setWeight(detail.getWeight());
        info.setPhotoUrl(
                detail.getSprites() != null ? detail.getSprites().getFront_default() : null);
        info.setType(
                detail.getTypes() != null && !detail.getTypes().isEmpty()
                        ? detail.getTypes().get(0).getType().getName()
                        : null);

        // Set the abilities of the Pokémon
        info.setAbilities(
                detail.getAbilities() != null
                        ? detail.getAbilities().stream()
                                .map(ability -> ability.getAbility().getName())
                                .toList()
                        : new ArrayList<>());

        LOGGER.debug(LOGLINE, "mapToPokemonInfo", Constants.OUT);
        return info;
    }


    private <T> ServiceResponse<T> buildErrorResponse(String message, HttpStatus status) {
    LOGGER.error(message);
    return new ServiceResponse<>(Status.KO, null, message, status);
}

}
