package com.pokedex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.pokedex.config.AppConfig;
import com.pokedex.dto.PokemonDto;
import com.pokedex.dto.PokemonItemDto;
import com.pokedex.dto.PokemonListDto;
import com.pokedex.enums.Status;
import com.pokedex.service.impl.PokeApiServiceImpl;

public class PokeApiServiceImplTest {
    @InjectMocks
    private AppConfig appConfig;
    
    @InjectMocks
    private PokeApiServiceImpl pokeApiService;

    

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pokeApiService.BASE_URL = "https://pokeapi.co/api/v2"; // Asegura que la URL base sea absoluta
    }

    @Test
    void getPokemons_returnsPagedResponse() {
        PokemonListDto listDto = new PokemonListDto();
        PokemonItemDto item = new PokemonItemDto();
        item.setName("bulbasaur");
        item.setUrl("https://pokeapi.co/api/v2/pokemon/1/");
        listDto.setResults(List.of(item));
        listDto.setCount(1);

        PokemonDto detailDto = new PokemonDto();
        detailDto.setName("bulbasaur");
        detailDto.setWeight(69);
        // Agrega mocks para types, abilities y sprites según tu DTO

        when(restTemplate.getForObject(anyString(), eq(PokemonListDto.class))).thenReturn(listDto);
        when(restTemplate.getForObject(contains("pokemon/1/"), eq(PokemonDto.class))).thenReturn(detailDto);

        var response = pokeApiService.getPokemons(0, 1);

        assertEquals(Status.OK, response.getStatus());
        assertNotNull(response.getResponseObject());
        assertEquals(1, response.getResponseObject().getData().size());
        assertEquals("bulbasaur", response.getResponseObject().getData().get(0).getName());
    }

    @Test
    void getPokemonDetail_returnsPokemonInfo() {
        PokemonDto detailDto = new PokemonDto();
        detailDto.setName("bulbasaur");
        detailDto.setWeight(69);
        // Agrega mocks para types, abilities y sprites según tu DTO

        when(restTemplate.getForObject(contains("/pokemon/bulbasaur"), eq(PokemonDto.class))).thenReturn(detailDto);

        var response = pokeApiService.getPokemonDetail("bulbasaur");

        assertEquals(Status.OK, response.getStatus());
        assertNotNull(response.getResponseObject());
        assertEquals("bulbasaur", response.getResponseObject().getName());
    }
/* 
    @Test
    void getPokemons_returnsErrorWhenApiReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(PokemonListDto.class))).thenReturn(null);

        var response = pokeApiService.getPokemons(0, 1);

        
        assertNull(response.getResponseObject());
    }
    */
    
}
