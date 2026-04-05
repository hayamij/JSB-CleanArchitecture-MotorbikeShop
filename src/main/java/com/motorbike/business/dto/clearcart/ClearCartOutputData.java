package com.motorbike.business.dto.clearcart;

public class ClearCartOutputData {
    private final boolean success;
    private final Long cartId;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ClearCartOutputData(Long cartId) {
        this.success = true;
        this.cartId = cartId;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    public ClearCartOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.cartId = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getCartId() {
        return cartId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static ClearCartOutputData forError(String errorCode, String errorMessage) {
        return new ClearCartOutputData(errorCode, errorMessage);
    }
}
