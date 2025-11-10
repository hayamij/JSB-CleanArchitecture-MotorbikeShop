package com.motorbike.interfaceadapters.dto;

/**
 * CheckoutResponseDTO - Interface Adapters Layer
 * DTO for checkout response
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class CheckoutResponseDTO {
    
    private OrderDTO order;
    private String message;
    private boolean success;
    
    public CheckoutResponseDTO() {
    }
    
    public CheckoutResponseDTO(OrderDTO order, String message, boolean success) {
        this.order = order;
        this.message = message;
        this.success = success;
    }
    
    public OrderDTO getOrder() {
        return order;
    }
    
    public void setOrder(OrderDTO order) {
        this.order = order;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
