package com.pokedex.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonListDto {
    private int count;
    private String next;
    private String previous;
    private List<PokemonItemDto> results;



}
