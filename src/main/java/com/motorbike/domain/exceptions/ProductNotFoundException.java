package com.motorbike.domain.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private final String errorCode;
    
    public ProductNotFoundException(String productId) {
        super("Không tìm thấy sản phẩm với mã: " + productId);
        this.errorCode = "PRODUCT_NOT_FOUND";
    }
    
    public String getErrorCode() {return errorCode;}
}
