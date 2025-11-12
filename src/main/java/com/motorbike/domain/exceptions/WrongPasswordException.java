package com.motorbike.domain.exceptions;

public class WrongPasswordException extends RuntimeException {
    private final String errorCode;
    
    public WrongPasswordException() {
        super("Mật khẩu không đúng");
        this.errorCode = "WRONG_PASSWORD";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
