package com.motorbike.business.dto.motorbike;

public class DeleteMotorbikeOutputData {

    public boolean success;
    public String errorCode;
    public String errorMessage;

    public DeleteMotorbikeOutputData(boolean success) {
        this.success = success;
    }

    public DeleteMotorbikeOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
