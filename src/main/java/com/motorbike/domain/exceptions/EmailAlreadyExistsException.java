package com.motorbike.domain.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    private final String errorCode;
    
    public EmailAlreadyExistsException(String email) {
        super("Email đã được sử dụng: " + email);
        this.errorCode = "EMAIL_EXISTS";
    }
    
    public String getErrorCode() {return errorCode;}
}
