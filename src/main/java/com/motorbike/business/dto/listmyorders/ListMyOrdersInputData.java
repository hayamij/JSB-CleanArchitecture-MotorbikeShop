package com.motorbike.business.dto.listmyorders;

public class ListMyOrdersInputData {
    private final Long userId;
    
    public ListMyOrdersInputData(Long userId) {
        this.userId = userId;
    }
    
    public Long getUserId() {
        return userId;
    }
}
