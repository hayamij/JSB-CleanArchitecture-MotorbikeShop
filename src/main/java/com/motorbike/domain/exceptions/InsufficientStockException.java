package com.motorbike.domain.exceptions;

public class InsufficientStockException extends RuntimeException {
    private final String errorCode;
    
    public InsufficientStockException(int available) {
        super("Không đủ hàng trong kho. Còn lại: " + available);
        this.errorCode = "INSUFFICIENT_STOCK";
    }
    
    public InsufficientStockException(String productName, int available) {
        super("Không đủ hàng cho sản phẩm: " + productName + ". Còn lại: " + available);
        this.errorCode = "INSUFFICIENT_STOCK";
    }
    
    public String getErrorCode() {return errorCode;}
}
