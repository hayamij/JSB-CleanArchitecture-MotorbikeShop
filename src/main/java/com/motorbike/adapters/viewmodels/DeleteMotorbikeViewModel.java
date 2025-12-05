package com.motorbike.adapters.viewmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteMotorbikeViewModel {

    public boolean success = false;
    public String errorCode;
    public String errorMessage;

    public DeleteMotorbikeViewModel() {}

    public DeleteMotorbikeViewModel(boolean success) {
        this.success = success;
    }

    public DeleteMotorbikeViewModel(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
