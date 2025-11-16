package com.motorbike.domain.exceptions;

/**
 * Exception for system errors
 * Used when an unexpected system error occurs
 */
public class SystemException extends RuntimeException {
    private final String errorCode;
    
    public SystemException() {
        super("Đã xảy ra lỗi hệ thống");
        this.errorCode = "SYSTEM_ERROR";
    }
    
    public SystemException(Throwable cause) {
        super("Đã xảy ra lỗi hệ thống", cause);
        this.errorCode = "SYSTEM_ERROR";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
