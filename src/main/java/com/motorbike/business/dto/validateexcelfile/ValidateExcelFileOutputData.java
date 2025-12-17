package com.motorbike.business.dto.validateexcelfile;

import java.util.ArrayList;
import java.util.List;

public class ValidateExcelFileOutputData {
    private final boolean success;
    private final boolean isValid;
    private final List<String> errors;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ValidateExcelFileOutputData(boolean isValid, List<String> errors) {
        this.success = true;
        this.isValid = isValid;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
        this.errorCode = null;
        // Set errorMessage from errors list if not valid
        this.errorMessage = (errors != null && !errors.isEmpty()) ? String.join("; ", errors) : null;
    }

    // Error constructor
    private ValidateExcelFileOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.isValid = false;
        this.errors = new ArrayList<>();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ValidateExcelFileOutputData forError(String errorCode, String errorMessage) {
        return new ValidateExcelFileOutputData(errorCode, errorMessage);
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
