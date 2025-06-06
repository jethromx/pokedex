package com.pokedex.service;


import com.pokedex.dto.PagedResponse;
import com.pokedex.dto.ServiceResponse;
import com.pokedex.entity.PokemonInfo;

public interface PokeApiService {
      // Get a paginated list of Pokemons
      public ServiceResponse<PagedResponse<PokemonInfo>> getPokemons(int offset, int limit); 
      // Get detailed information about a specific Pokemon by name or ID
      public ServiceResponse<PokemonInfo> getPokemonDetail(String nameOrId);
    
}
