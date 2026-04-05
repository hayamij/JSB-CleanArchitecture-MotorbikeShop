package com.motorbike.business.dto.checkproductduplication;

public class CheckProductDuplicationOutputData {
    private final boolean success;
    private final boolean isDuplicate;
    private final String duplicatedField; // "name", "code", or null
    private final Long existingProductId;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public CheckProductDuplicationOutputData(boolean isDuplicate, String duplicatedField, Long existingProductId) {
        this.success = true;
        this.isDuplicate = isDuplicate;
        this.duplicatedField = duplicatedField;
        this.existingProductId = existingProductId;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private CheckProductDuplicationOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.isDuplicate = false;
        this.duplicatedField = null;
        this.existingProductId = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static CheckProductDuplicationOutputData forError(String errorCode, String errorMessage) {
        return new CheckProductDuplicationOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public String getDuplicatedField() {
        return duplicatedField;
    }

    public Long getExistingProductId() {
        return existingProductId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
