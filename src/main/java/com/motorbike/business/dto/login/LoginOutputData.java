package com.motorbike.business.dto.login;

import com.motorbike.domain.entities.VaiTro;
import java.time.LocalDateTime;

public class LoginOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long userId;
    private final String email;
    private final String username;
    private final VaiTro role;
    private final LocalDateTime lastLoginAt;
    
    private final String sessionToken;
    private final Long cartId;
    
    private final boolean cartMerged;
    private final int mergedItemsCount;

    public LoginOutputData(Long userId, String email, String username,
                          VaiTro role, LocalDateTime lastLoginAt,
                          String sessionToken, Long cartId, boolean cartMerged, int mergedItemsCount) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.lastLoginAt = lastLoginAt;
        this.sessionToken = sessionToken;
        this.cartId = cartId;
        this.cartMerged = cartMerged;
        this.mergedItemsCount = mergedItemsCount;
    }

    public LoginOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.userId = null;
        this.email = null;
        this.username = null;
        this.role = null;
        this.lastLoginAt = null;
        this.sessionToken = null;
        this.cartId = null;
        this.cartMerged = false;
        this.mergedItemsCount = 0;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {return errorCode;}

    public String getErrorMessage() {return errorMessage;}

    public Long getUserId() {return userId;}

    public String getEmail() {return email;}

    public String getUsername() {return username;}

    public VaiTro getRole() {return role;}

    public LocalDateTime getLastLoginAt() {return lastLoginAt;}

    public String getSessionToken() {return sessionToken;}

    public Long getCartId() {return cartId;}

    public boolean isCartMerged() {
        return cartMerged;
    }

    public int getMergedItemsCount() {return mergedItemsCount;}
    
    public static LoginOutputData forSuccess(
            Long userId, String email, String username,
            VaiTro role, LocalDateTime lastLoginAt,
            String sessionToken, Long cartId, boolean cartMerged, int mergedItemsCount) {
        return new LoginOutputData(userId, email, username, role, lastLoginAt,
                sessionToken, cartId, cartMerged, mergedItemsCount);
    }
    
    public static LoginOutputData forError(String errorCode, String errorMessage) {
        return new LoginOutputData(errorCode, errorMessage);
    }
}
