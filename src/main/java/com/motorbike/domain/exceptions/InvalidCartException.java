package com.motorbike.domain.exceptions;

public class InvalidCartException extends BusinessException {
    
    public InvalidCartException(String errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidCartException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
