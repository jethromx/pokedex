package com.pokedex.enums;

public enum Status {
    OK("OK"),
    KO("KO");
 
    private String value;
 
    private Status(final String value) {
       this.value = value;
    }
 
    public String getValue() {
       return this.value;
    }
 }

