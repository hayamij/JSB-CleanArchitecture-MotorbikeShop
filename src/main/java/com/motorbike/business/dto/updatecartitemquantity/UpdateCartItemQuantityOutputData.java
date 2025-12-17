package com.motorbike.business.dto.updatecartitemquantity;

public class UpdateCartItemQuantityOutputData {
    private final boolean success;
    private final String message;
    private final int newQuantity;
    
    public UpdateCartItemQuantityOutputData(boolean success, String message, int newQuantity) {
        this.success = success;
        this.message = message;
        this.newQuantity = newQuantity;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public int getNewQuantity() {
        return newQuantity;
    }
}
