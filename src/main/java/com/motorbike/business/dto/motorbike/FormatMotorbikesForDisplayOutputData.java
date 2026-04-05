package com.motorbike.business.dto.motorbike;

import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData.MotorbikeItem;
import java.util.List;

/**
 * UC-74: Format Motorbikes For Display
 * Output data with formatted motorbike items for display
 */
public class FormatMotorbikesForDisplayOutputData {
    
    private final boolean success;
    private final List<MotorbikeItem> motorbikeItems;
    private final String errorCode;
    private final String errorMessage;
    
    private FormatMotorbikesForDisplayOutputData(boolean success, List<MotorbikeItem> motorbikeItems,
                                                  String errorCode, String errorMessage) {
        this.success = success;
        this.motorbikeItems = motorbikeItems;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static FormatMotorbikesForDisplayOutputData forSuccess(List<MotorbikeItem> motorbikeItems) {
        return new FormatMotorbikesForDisplayOutputData(true, motorbikeItems, null, null);
    }
    
    public static FormatMotorbikesForDisplayOutputData forError(String errorCode, String errorMessage) {
        return new FormatMotorbikesForDisplayOutputData(false, null, errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public List<MotorbikeItem> getMotorbikeItems() {
        return motorbikeItems;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
