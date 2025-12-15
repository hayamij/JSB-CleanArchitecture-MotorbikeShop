package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.accessory.AddAccessoryOutputData.AccessoryItem;

public class AddAccessoryViewModel {
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public AccessoryItem accessory;
}
