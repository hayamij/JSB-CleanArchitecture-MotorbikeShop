package com.motorbike.adapters.dto.request;

/**
 * Request DTO for updating cart quantity
 */
public class UpdateCartRequest {
    private Long cartId;
    private Long productId;
    private int newQuantity;

    // Getters and Setters
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public int getNewQuantity() { return newQuantity; }
    public void setNewQuantity(int newQuantity) { this.newQuantity = newQuantity; }
}
