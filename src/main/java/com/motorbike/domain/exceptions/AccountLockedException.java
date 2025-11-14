package com.motorbike.domain.exceptions;

public class AccountLockedException extends RuntimeException {
    private final String errorCode;
    
    public AccountLockedException() {
        super("Tài khoản đã bị khóa. Vui lòng liên hệ admin.");
        this.errorCode = "ACCOUNT_LOCKED";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
