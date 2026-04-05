package com.motorbike.business.dto.validateimportrow;

import java.util.ArrayList;
import java.util.List;

public class ValidateImportRowOutputData {
    private final boolean success;
    private final boolean isValid;
    private final int rowNumber;
    private final List<String> errors;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ValidateImportRowOutputData(boolean isValid, int rowNumber, List<String> errors) {
        this.success = true;
        this.isValid = isValid;
        this.rowNumber = rowNumber;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private ValidateImportRowOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.isValid = false;
        this.rowNumber = 0;
        this.errors = new ArrayList<>();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ValidateImportRowOutputData forError(String errorCode, String errorMessage) {
        return new ValidateImportRowOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isValid() {
        return isValid;
    }

    public int getRowNumber() {
        return rowNumber;
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
