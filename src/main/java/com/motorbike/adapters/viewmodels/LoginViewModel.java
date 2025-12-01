package com.motorbike.adapters.viewmodels;

public class LoginViewModel {
    
    public boolean success;
    public String message;
    
    public Long userId;
    public String email;
    public String username;
    public String roleDisplay;
    public String lastLoginDisplay;
    
    public String sessionToken;
    public Long cartId;
    
    public boolean cartMerged;
    public int mergedItemsCount;
    public String cartMergeMessage;
    
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor;
    
    public LoginViewModel() {
        this.success = false;
        this.hasError = false;
    }
}
