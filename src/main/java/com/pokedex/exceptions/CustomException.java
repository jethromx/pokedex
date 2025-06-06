package com.pokedex.exceptions;

import java.sql.Date;

public class CustomException extends RuntimeException {
    private final String errorCode;
    private final Date timestamp = new Date(System.currentTimeMillis());

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}