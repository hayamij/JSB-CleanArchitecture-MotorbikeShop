package com.motorbike.domain.exceptions;

/**
 * Exception thrown when authentication fails
 */
public class AuthenticationException extends BusinessException {
    
    public AuthenticationException(String errorCode, String message) {
        super(errorCode, message);
    }

    public AuthenticationException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
