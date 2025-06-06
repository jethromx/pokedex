package com.pokedex.entity;

import java.util.List;

import lombok.Data;

@Data
public class Chain {
    private boolean isBaby;
    private Species species;
    private List<Chain> evolves_to;
    
    
}
