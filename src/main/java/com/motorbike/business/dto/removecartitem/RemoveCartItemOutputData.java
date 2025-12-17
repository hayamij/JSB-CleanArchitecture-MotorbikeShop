package com.motorbike.business.dto.removecartitem;

public class RemoveCartItemOutputData {
    private final boolean success;
    private final String message;
    
    public RemoveCartItemOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}
