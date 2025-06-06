package com.pokedex.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonInfo {
        private int id;
        private String name;
        private String photoUrl;
        private String type;
        private int weight;
        private List<String> abilities;       
        private String baseHapinnes;
        private String captureRate;
        private String color;
        private List<String> eggGroups;
        private List<String> evolutions;
    }
