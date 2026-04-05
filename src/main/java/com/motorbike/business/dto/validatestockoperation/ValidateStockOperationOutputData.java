package com.motorbike.business.dto.validatestockoperation;

public class ValidateStockOperationOutputData {
    private final boolean success;
    private final boolean isValid;
    private final String reason;
    private final int resultingStock;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ValidateStockOperationOutputData(boolean isValid, String reason, int resultingStock) {
        this.success = true;
        this.isValid = isValid;
        this.reason = reason;
        this.resultingStock = resultingStock;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private ValidateStockOperationOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.isValid = false;
        this.reason = null;
        this.resultingStock = 0;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ValidateStockOperationOutputData forError(String errorCode, String errorMessage) {
        return new ValidateStockOperationOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getReason() {
        return reason;
    }

    public int getResultingStock() {
        return resultingStock;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
