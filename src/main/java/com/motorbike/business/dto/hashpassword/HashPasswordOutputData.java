package com.motorbike.business.dto.hashpassword;

public class HashPasswordOutputData {
    private final boolean success;
    private final String hashedPassword;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public HashPasswordOutputData(String hashedPassword) {
        this.success = true;
        this.hashedPassword = hashedPassword;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private HashPasswordOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.hashedPassword = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static HashPasswordOutputData forError(String errorCode, String errorMessage) {
        return new HashPasswordOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
