package com.motorbike.domain.exceptions;

/**
 * Exception for User-related business rule violations
 */
public class InvalidUserException extends BusinessException {
    
    public InvalidUserException(String errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidUserException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
