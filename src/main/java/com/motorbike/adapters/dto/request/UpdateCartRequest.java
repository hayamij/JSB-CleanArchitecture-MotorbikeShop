package com.motorbike.adapters.dto.request;

/**
 * Request DTO for updating cart quantity
 */
public class UpdateCartRequest {
    private Long userId;
    private Long productId;
    private int newQuantity;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public int getNewQuantity() { return newQuantity; }
    public void setNewQuantity(int newQuantity) { this.newQuantity = newQuantity; }
}
