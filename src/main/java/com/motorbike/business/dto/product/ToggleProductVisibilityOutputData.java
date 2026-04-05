package com.motorbike.business.dto.product;

public class ToggleProductVisibilityOutputData {
    private final boolean success;
    private final boolean isVisible;
    private final String message;
    private final String errorCode;
    private final String errorMessage;

    private ToggleProductVisibilityOutputData(boolean success, boolean isVisible, String message,
                                              String errorCode, String errorMessage) {
        this.success = success;
        this.isVisible = isVisible;
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ToggleProductVisibilityOutputData forSuccess(boolean isVisible, String message) {
        return new ToggleProductVisibilityOutputData(true, isVisible, message, null, null);
    }

    public static ToggleProductVisibilityOutputData forError(String errorCode, String errorMessage) {
        return new ToggleProductVisibilityOutputData(false, false, null, errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
