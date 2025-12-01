package com.motorbike.domain.exceptions;

public class CartNotFoundException extends RuntimeException {
    private final String errorCode;
    
    public CartNotFoundException() {
        super("Không tìm thấy giỏ hàng");
        this.errorCode = "CART_NOT_FOUND";
    }
    
    public String getErrorCode() {return errorCode;}
}
