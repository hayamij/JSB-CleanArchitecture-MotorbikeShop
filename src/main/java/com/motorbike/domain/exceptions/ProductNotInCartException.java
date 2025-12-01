package com.motorbike.domain.exceptions;

public class ProductNotInCartException extends RuntimeException {
    private final String errorCode;
    
    public ProductNotInCartException() {
        super("Sản phẩm không có trong giỏ hàng");
        this.errorCode = "PRODUCT_NOT_IN_CART";
    }
    
    public ProductNotInCartException(String message) {
        super(message);
        this.errorCode = "PRODUCT_NOT_IN_CART";
    }
    
    public String getErrorCode() {return errorCode;}
}
