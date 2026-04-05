package com.motorbike.business.dto.cart;

public class CreateUserCartOutputData {
    private final boolean success;
    private final Long cartId;
    private final Long userId;
    private final String errorCode;
    private final String errorMessage;
    
    private CreateUserCartOutputData(boolean success, Long cartId, Long userId, 
                                    String errorCode, String errorMessage) {
        this.success = success;
        this.cartId = cartId;
        this.userId = userId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static CreateUserCartOutputData forSuccess(Long cartId, Long userId) {
        return new CreateUserCartOutputData(true, cartId, userId, null, null);
    }
    
    public static CreateUserCartOutputData forError(String errorCode, String errorMessage) {
        return new CreateUserCartOutputData(false, null, null, errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public Long getCartId() {
        return cartId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
