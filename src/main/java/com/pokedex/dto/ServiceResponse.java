package com.pokedex.dto;

import org.springframework.http.HttpStatus;

import com.pokedex.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse<T> {

    private Status status;

    private T responseObject;

    private String message;

    private HttpStatus httpCode;

   
   

}
