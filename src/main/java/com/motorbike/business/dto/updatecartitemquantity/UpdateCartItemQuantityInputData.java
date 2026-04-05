package com.motorbike.business.dto.updatecartitemquantity;

public class UpdateCartItemQuantityInputData {
    private final Long userId;
    private final Long productId;
    private final int newQuantity;
    
    public UpdateCartItemQuantityInputData(Long userId, Long productId, int newQuantity) {
        this.userId = userId;
        this.productId = productId;
        this.newQuantity = newQuantity;
    }

    // Constructor with GioHang and int (for backward compatibility)
    public UpdateCartItemQuantityInputData(com.motorbike.domain.entities.GioHang gioHang, int newQuantity) {
        this.userId = gioHang != null ? gioHang.getMaTaiKhoan() : null;
        this.productId = null;  // Will be determined from cart
        this.newQuantity = newQuantity;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public int getNewQuantity() {
        return newQuantity;
    }
}
