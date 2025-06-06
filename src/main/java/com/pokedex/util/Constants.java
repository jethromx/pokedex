package com.pokedex.util;

public class Constants {

    // Private constructor to prevent instantiation
    private Constants() {
        throw new IllegalStateException("Cannot instantiate a constant class.");
    }


    // For messages
    public static final String ERROR = "ERROR";
    public static final String WARN = "WARNING";
    public static final String IN = "IN";
    public static final String OUT = "OUT";
    public static final String METHOD_GET = "get";
    public static final String METHOD_LIST = "list";


    public static final String CACHE_LIST_POKEMONS = "cacheListPokemons";
    public static final String CACHE_DETAIL_POKEMON = "cacheDetailPokemon";
}
