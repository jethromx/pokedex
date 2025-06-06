package com.pokedex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class Sprites {
    private String front_default;
    private String front_shiny;
    private String back_default;
    private String back_shiny;

    
}
