package com.pokedex.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.pokedex.exceptions.CustomException;

// This class is a global exception handler for the application.
// It can be used to handle exceptions thrown by controllers and provide a unified response format.
@ControllerAdvice
public class GlobalExceptionHandler {

   
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
       HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> error = buildErrorResponse(ex.getMessage(), "400", status);

        return ResponseEntity.status(status).body(error);
    }

 
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(HttpMessageNotReadableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> error = buildErrorResponse(ex.getMessage(), "400", status);
        
        return ResponseEntity.status(status).body(error);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NoResourceFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, String> error = buildErrorResponse(ex.getMessage(), "404", status);

        return ResponseEntity.status(status).body(error);
    }

  
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(ResourceAccessException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, String> error = buildErrorResponse(ex.getMessage(), "500", status);
        
        return ResponseEntity.status(status).body(error);
    }


    
    // This method handles custom exceptions thrown by the application.
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(CustomException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> error  = buildErrorResponse(ex.getMessage(),ex.getErrorCode(),status);
       
      
        if(ex.getErrorCode().equals("001")) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            error  = buildErrorResponse(ex.getMessage(),ex.getErrorCode(),status);
            return ResponseEntity.status(status).body(error);
        }

        return ResponseEntity.status(status).body(error);
    }


    // build message to response
    private Map<String, String> buildErrorResponse(String message, String errorCode, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(Calendar.getInstance().getTime());
        error.put("timestamp", s);
        error.put("error", message);
        error.put("errorCode", errorCode);
        return error;
    }
    
}
