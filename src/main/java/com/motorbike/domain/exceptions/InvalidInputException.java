package com.motorbike.domain.exceptions;

public abstract class InvalidInputException extends IllegalArgumentException {
    private final String errorCode;
    
    protected InvalidInputException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {return errorCode;}
}
