package com.motorbike.business.dto.checkuserduplication;

public class CheckUserDuplicationOutputData {
    private final boolean success;
    private final boolean isDuplicate;
    private final String duplicatedField; // "email", "username", or null
    private final Long existingUserId;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public CheckUserDuplicationOutputData(boolean isDuplicate, String duplicatedField, Long existingUserId) {
        this.success = true;
        this.isDuplicate = isDuplicate;
        this.duplicatedField = duplicatedField;
        this.existingUserId = existingUserId;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private CheckUserDuplicationOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.isDuplicate = false;
        this.duplicatedField = null;
        this.existingUserId = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static CheckUserDuplicationOutputData forError(String errorCode, String errorMessage) {
        return new CheckUserDuplicationOutputData(errorCode, errorMessage);
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

    public Long getExistingUserId() {
        return existingUserId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
