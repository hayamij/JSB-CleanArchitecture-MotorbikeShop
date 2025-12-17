package com.motorbike.business.dto.validateuserregistration;

import java.util.ArrayList;
import java.util.List;

public class ValidateUserRegistrationOutputData {
    private final boolean success;
    private final boolean isValid;
    private final List<String> errors;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ValidateUserRegistrationOutputData(boolean isValid, List<String> errors) {
        this.success = true;
        this.isValid = isValid;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private ValidateUserRegistrationOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.isValid = false;
        this.errors = new ArrayList<>();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ValidateUserRegistrationOutputData forError(String errorCode, String errorMessage) {
        return new ValidateUserRegistrationOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isValid() {
        return isValid;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
