package com.motorbike.business.dto.validateordercancellation;

public class ValidateOrderCancellationOutputData {
    private final boolean canCancel;
    private final String reason;
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    public ValidateOrderCancellationOutputData(boolean canCancel, String reason) {
        this.canCancel = canCancel;
        this.reason = reason;
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    private ValidateOrderCancellationOutputData(String errorCode, String errorMessage) {
        this.canCancel = false;
        this.reason = null;
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static ValidateOrderCancellationOutputData forError(String errorCode, String errorMessage) {
        return new ValidateOrderCancellationOutputData(errorCode, errorMessage);
    }
    
    public boolean isSuccess() { return success; }
    public boolean canCancel() { return canCancel; }
    public String getReason() { return reason; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
