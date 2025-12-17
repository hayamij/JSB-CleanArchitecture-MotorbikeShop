package com.motorbike.business.dto.verifypassword;

public class VerifyPasswordOutputData {
    private final boolean success;
    private final boolean isValid;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public VerifyPasswordOutputData(boolean isValid) {
        this.success = true;
        this.isValid = isValid;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private VerifyPasswordOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.isValid = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static VerifyPasswordOutputData forError(String errorCode, String errorMessage) {
        return new VerifyPasswordOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
