package com.motorbike.interfaceadapters.dto;

/**
 * UpdateCartQuantityRequestDTO - Interface Adapters Layer
 * DTO for update cart quantity request
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class UpdateCartQuantityRequestDTO {
    
    private Long userId;
    private Long productId;
    private Integer newQuantity;
    
    public UpdateCartQuantityRequestDTO() {
    }
    
    public UpdateCartQuantityRequestDTO(Long userId, Long productId, Integer newQuantity) {
        this.userId = userId;
        this.productId = productId;
        this.newQuantity = newQuantity;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getNewQuantity() {
        return newQuantity;
    }
    
    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }
}
