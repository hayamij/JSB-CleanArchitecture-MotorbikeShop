package com.motorbike.domain.exceptions;

public class InvalidProductException extends BusinessException {
    
    public InvalidProductException(String errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidProductException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
