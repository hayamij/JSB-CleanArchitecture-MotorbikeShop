package com.motorbike.domain.exceptions;

public class UserNotFoundException extends RuntimeException {
    private final String errorCode;
    
    public UserNotFoundException(String email) {
        super("Không tìm thấy tài khoản với email: " + email);
        this.errorCode = "USER_NOT_FOUND";
    }
    
    public String getErrorCode() {return errorCode;}
}
