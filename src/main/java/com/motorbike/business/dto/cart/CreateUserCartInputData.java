package com.motorbike.business.dto.cart;

public class CreateUserCartInputData {
    private final Long userId;
    
    public CreateUserCartInputData(Long userId) {
        this.userId = userId;
    }
    
    public Long getUserId() {
        return userId;
    }
}
