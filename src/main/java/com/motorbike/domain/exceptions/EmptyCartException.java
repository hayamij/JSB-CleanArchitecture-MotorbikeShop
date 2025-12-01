package com.motorbike.domain.exceptions;

public class EmptyCartException extends RuntimeException {
    private final String errorCode;
    
    public EmptyCartException() {
        super("Giỏ hàng trống");
        this.errorCode = "EMPTY_CART";
    }
    
    public String getErrorCode() {return errorCode;}
}
