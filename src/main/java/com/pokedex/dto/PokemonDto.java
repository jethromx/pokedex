package com.pokedex.dto;

import java.util.List;

import com.pokedex.entity.AbilitySlot;
import com.pokedex.entity.Sprites;
import com.pokedex.entity.TypeSlot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonDto {
    private int id;
    private String name;
    private int weight;
    private List<TypeSlot> types;
    private List<AbilitySlot> abilities;
    private Sprites sprites;



}
