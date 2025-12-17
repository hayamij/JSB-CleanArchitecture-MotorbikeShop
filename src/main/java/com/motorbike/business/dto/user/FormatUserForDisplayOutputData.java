package com.motorbike.business.dto.user;

import com.motorbike.business.dto.user.SearchUsersOutputData.UserItem;
import java.util.List;

/**
 * UC-54: Format User For Display
 * Output data with formatted user items for display
 */
public class FormatUserForDisplayOutputData {
    
    private final boolean success;
    private final List<UserItem> userItems;
    private final String errorCode;
    private final String errorMessage;
    
    private FormatUserForDisplayOutputData(boolean success, List<UserItem> userItems, 
                                            String errorCode, String errorMessage) {
        this.success = success;
        this.userItems = userItems;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static FormatUserForDisplayOutputData forSuccess(List<UserItem> userItems) {
        return new FormatUserForDisplayOutputData(true, userItems, null, null);
    }
    
    public static FormatUserForDisplayOutputData forError(String errorCode, String errorMessage) {
        return new FormatUserForDisplayOutputData(false, null, errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public List<UserItem> getUserItems() {
        return userItems;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
