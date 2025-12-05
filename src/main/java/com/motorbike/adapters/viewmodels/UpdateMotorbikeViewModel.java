package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData.MotorbikeItem;

public class UpdateMotorbikeViewModel {

    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;

    public MotorbikeItem motorbike;   // ❗ BẮT BUỘC PHẢI CÓ FIELD NÀY

    public UpdateMotorbikeViewModel() {}

    public UpdateMotorbikeViewModel(String errorCode, String errorMessage) {
        this.hasError = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public UpdateMotorbikeViewModel(MotorbikeItem motorbike) {
        this.motorbike = motorbike;
    }
}
