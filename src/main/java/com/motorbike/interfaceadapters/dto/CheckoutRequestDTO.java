package com.motorbike.interfaceadapters.dto;

/**
 * CheckoutRequestDTO - Interface Adapters Layer
 * DTO for checkout request
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class CheckoutRequestDTO {
    
    private Long userId;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPhone;
    private String paymentMethod;
    
    public CheckoutRequestDTO() {
    }
    
    public CheckoutRequestDTO(Long userId, String shippingAddress, String shippingCity, 
                             String shippingPhone, String paymentMethod) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingPhone = shippingPhone;
        this.paymentMethod = paymentMethod;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getShippingCity() {
        return shippingCity;
    }
    
    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }
    
    public String getShippingPhone() {
        return shippingPhone;
    }
    
    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
