package com.motorbike.adapters.viewmodels;

/**
 * View Model for Register Feature
 * Contains already-formatted data ready for UI display
 * NO business logic - pure data container
 */
public class RegisterViewModel {
    
    // Success state
    public boolean success;
    public String message;
    
    // User information (only if success)
    public Long userId;
    public String email;
    public String username;
    public String roleDisplay; // "Khách hàng"
    public String registeredAtDisplay; // Formatted datetime
    
    // Auto-login information
    public boolean autoLoginEnabled;
    public String sessionToken;
    public String redirectUrl; // Where to redirect after registration
    
    // Error information (only if not success)
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor; // For UI styling
    
    // Field-specific errors (for form validation display)
    public String emailError;
    public String usernameError;
    public String passwordError;
    public String phoneError;
    
    // Constructor
    public RegisterViewModel() {
        this.success = false;
        this.hasError = false;
        this.autoLoginEnabled = false;
    }
}
