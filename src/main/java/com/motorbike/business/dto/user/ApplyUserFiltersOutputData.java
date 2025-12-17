package com.motorbike.business.dto.user;

import com.motorbike.domain.entities.TaiKhoan;
import java.util.List;

/**
 * UC-73: Apply User Filters
 * Output data with filtered user list
 */
public class ApplyUserFiltersOutputData {
    
    private final boolean success;
    private final List<TaiKhoan> filteredUsers;
    private final String errorCode;
    private final String errorMessage;
    
    private ApplyUserFiltersOutputData(boolean success, List<TaiKhoan> filteredUsers, 
                                        String errorCode, String errorMessage) {
        this.success = success;
        this.filteredUsers = filteredUsers;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static ApplyUserFiltersOutputData forSuccess(List<TaiKhoan> filteredUsers) {
        return new ApplyUserFiltersOutputData(true, filteredUsers, null, null);
    }
    
    public static ApplyUserFiltersOutputData forError(String errorCode, String errorMessage) {
        return new ApplyUserFiltersOutputData(false, null, errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public List<TaiKhoan> getFilteredUsers() {
        return filteredUsers;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
