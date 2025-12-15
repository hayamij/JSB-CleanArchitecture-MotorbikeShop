package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.accessory.UpdateAccessoryOutputData.AccessoryItem;

public class UpdateAccessoryViewModel {
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public AccessoryItem accessory;
}
