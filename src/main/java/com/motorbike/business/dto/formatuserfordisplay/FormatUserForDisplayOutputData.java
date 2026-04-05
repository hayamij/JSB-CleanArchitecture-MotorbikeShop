package com.motorbike.business.dto.formatuserfordisplay;

public class FormatUserForDisplayOutputData {
    private final boolean success;
    private final Long userId;
    private final String username;
    private final String email;
    private final String fullName;
    private final String phoneNumber;
    private final String roleDisplay;
    private final String roleBadgeColor;
    private final String formattedCreatedDate;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public FormatUserForDisplayOutputData(
            Long userId,
            String username,
            String email,
            String fullName,
            String phoneNumber,
            String roleDisplay,
            String roleBadgeColor,
            String formattedCreatedDate) {
        this.success = true;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.roleDisplay = roleDisplay;
        this.roleBadgeColor = roleBadgeColor;
        this.formattedCreatedDate = formattedCreatedDate;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private FormatUserForDisplayOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.userId = null;
        this.username = null;
        this.email = null;
        this.fullName = null;
        this.phoneNumber = null;
        this.roleDisplay = null;
        this.roleBadgeColor = null;
        this.formattedCreatedDate = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static FormatUserForDisplayOutputData forError(String errorCode, String errorMessage) {
        return new FormatUserForDisplayOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRoleDisplay() {
        return roleDisplay;
    }

    public String getRoleBadgeColor() {
        return roleBadgeColor;
    }

    public String getFormattedCreatedDate() {
        return formattedCreatedDate;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
