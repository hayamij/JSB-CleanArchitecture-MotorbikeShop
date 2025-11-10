package com.motorbike.business.usecase;

import com.motorbike.business.entity.Order;

/**
 * CheckoutUseCase - Business Layer
 * Use case interface for processing checkout and creating orders
 * Part of Clean Architecture - Business Layer
 */
public interface CheckoutUseCase {
    
    /**
     * Execute the checkout use case
     * @param request the request containing checkout information
     * @return the response containing created order
     */
    CheckoutResponse execute(CheckoutRequest request);
    
    /**
     * Request DTO for checkout
     */
    class CheckoutRequest {
        private final Long userId;
        private final String shippingAddress;
        private final String shippingCity;
        private final String shippingPhone;
        private final String paymentMethod;
        
        public CheckoutRequest(Long userId, String shippingAddress, String shippingCity, 
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
        
        public String getShippingAddress() {
            return shippingAddress;
        }
        
        public String getShippingCity() {
            return shippingCity;
        }
        
        public String getShippingPhone() {
            return shippingPhone;
        }
        
        public String getPaymentMethod() {
            return paymentMethod;
        }
    }
    
    /**
     * Response DTO for checkout
     */
    class CheckoutResponse {
        private final Order order;
        private final String message;
        private final boolean success;
        
        public CheckoutResponse(Order order, String message, boolean success) {
            this.order = order;
            this.message = message;
            this.success = success;
        }
        
        public Order getOrder() {
            return order;
        }
        
        public String getMessage() {
            return message;
        }
        
        public boolean isSuccess() {
            return success;
        }
    }
}
