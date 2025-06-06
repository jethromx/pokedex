package com.pokedex.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

@Getter
@Configuration
public class AppConfig {

   
    @Value("${POKEMON_API}")
    private String baseUrl;

}