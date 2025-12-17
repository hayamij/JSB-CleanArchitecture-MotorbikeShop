package com.motorbike.domain.exceptions;

public class SystemException extends RuntimeException {
    private final String errorCode;
    
    public SystemException() {
        super("Đã xảy ra lỗi hệ thống");
        this.errorCode = "SYSTEM_ERROR";
    }
    
    public SystemException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public SystemException(Throwable cause) {
        super("Đã xảy ra lỗi hệ thống", cause);
        this.errorCode = "SYSTEM_ERROR";
    }
    
    public String getErrorCode() {return errorCode;}
}
