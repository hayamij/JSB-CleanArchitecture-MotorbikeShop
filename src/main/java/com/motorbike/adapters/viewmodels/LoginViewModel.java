package com.motorbike.adapters.viewmodels;

/**
 * View Model for Login Feature
 * Contains already-formatted data ready for UI display
 * NO business logic - pure data container
 */
public class LoginViewModel {
    
    // Success state
    public boolean success;
    public String message;
    
    // User information (only if success)
    public Long userId;
    public String email;
    public String username;
    public String roleDisplay; // "Khách hàng" or "Quản trị viên"
    public String lastLoginDisplay; // Formatted datetime
    
    // Session info
    public String sessionToken;
    public Long cartId; // User's cart ID
    
    // Cart merge information
    public boolean cartMerged;
    public int mergedItemsCount; // Number of items merged from guest cart
    public String cartMergeMessage; // e.g., "Đã thêm 3 sản phẩm từ giỏ hàng tạm"
    
    // Error information (only if not success)
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor; // For UI styling
    
    // Constructor
    public LoginViewModel() {
        this.success = false;
        this.hasError = false;
    }
}
