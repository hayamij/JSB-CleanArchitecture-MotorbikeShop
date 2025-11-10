package com.motorbike.business.usecase.impl;

/**
 * Exception thrown when user account is not active
 */
public class UserNotActiveException extends RuntimeException {
    
    public UserNotActiveException(String message) {
        super(message);
    }
    
    public UserNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
