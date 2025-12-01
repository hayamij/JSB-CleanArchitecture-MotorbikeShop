package com.motorbike.adapters.viewmodels;

public class RegisterViewModel {
    
    public boolean success;
    public String message;
    
    public Long userId;
    public String email;
    public String username;
    public String roleDisplay;
    public String registeredAtDisplay;
    
    public boolean autoLoginEnabled;
    public String sessionToken;
    public String redirectUrl;
    
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor;
    
    public String emailError;
    public String usernameError;
    public String passwordError;
    public String phoneError;
    
    public RegisterViewModel() {
        this.success = false;
        this.hasError = false;
        this.autoLoginEnabled = false;
    }
}
