package com.pokedex.dto;

import java.util.List;

import com.pokedex.entity.Color;
import com.pokedex.entity.EggGroup;
import com.pokedex.entity.EvolutionChain;
import com.pokedex.entity.FlavorTextEntries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonSpeciesDto {

    String base_happiness;
    String capture_rate;
    Color color;
    List<EggGroup> egg_groups;
    List<FlavorTextEntries> flavor_text_entries;
    EvolutionChain evolution_chain;
}
