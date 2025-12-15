package com.motorbike.business.dto.accessory;

public class DeleteAccessoryOutputData {

    public final boolean success;
    public final String message;
    public final String errorCode;
    public final String errorMessage;

    public DeleteAccessoryOutputData(String message) {
        this.success = true;
        this.message = message;
        this.errorCode = null;
        this.errorMessage = null;
    }

    public DeleteAccessoryOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.message = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
