package com.motorbike.domain.exceptions;

public class CannotCancelOrderException extends RuntimeException {
    private final String errorCode;
    
    public CannotCancelOrderException(String message) {
        super(message);
        this.errorCode = "CANNOT_CANCEL_ORDER";
    }
    
    public CannotCancelOrderException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}