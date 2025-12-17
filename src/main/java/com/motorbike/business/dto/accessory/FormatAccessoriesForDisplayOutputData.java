package com.motorbike.business.dto.accessory;

import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData.AccessoryItem;
import java.util.List;

public class FormatAccessoriesForDisplayOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<AccessoryItem> accessoryItems;

    private FormatAccessoriesForDisplayOutputData(boolean success, String errorCode, String errorMessage, List<AccessoryItem> accessoryItems) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.accessoryItems = accessoryItems;
    }

    public static FormatAccessoriesForDisplayOutputData forSuccess(List<AccessoryItem> accessoryItems) {
        return new FormatAccessoriesForDisplayOutputData(true, null, null, accessoryItems);
    }

    public static FormatAccessoriesForDisplayOutputData forError(String errorCode, String errorMessage) {
        return new FormatAccessoriesForDisplayOutputData(false, errorCode, errorMessage, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<AccessoryItem> getAccessoryItems() {
        return accessoryItems;
    }
}
