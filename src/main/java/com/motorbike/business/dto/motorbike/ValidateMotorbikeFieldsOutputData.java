package com.motorbike.business.dto.motorbike;

import java.util.List;

/**
 * UC-75: Validate Motorbike Specific Fields
 * Output data with validation result
 */
public class ValidateMotorbikeFieldsOutputData {
    
    private final boolean success;
    private final boolean isValid;
    private final List<String> errors;
    private final String errorCode;
    private final String errorMessage;
    
    private ValidateMotorbikeFieldsOutputData(boolean success, boolean isValid, List<String> errors,
                                                String errorCode, String errorMessage) {
        this.success = success;
        this.isValid = isValid;
        this.errors = errors;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static ValidateMotorbikeFieldsOutputData forSuccess(boolean isValid, List<String> errors) {
        return new ValidateMotorbikeFieldsOutputData(true, isValid, errors, null, null);
    }
    
    public static ValidateMotorbikeFieldsOutputData forError(String errorCode, String errorMessage) {
        return new ValidateMotorbikeFieldsOutputData(false, false, null, errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public boolean isValid() {
        return isValid;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
