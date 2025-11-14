package com.motorbike.adapters.viewmodels;

/**
 * View Model for AddToCart Feature
 * Contains already-formatted data ready for UI display
 * NO business logic - pure data container
 */
public class AddToCartViewModel {
    
    // Success state
    public boolean success;
    public String message;
    
    // Cart summary
    public Long cartId;
    public int totalItems; // Number of different products
    public int totalQuantity; // Total quantity
    public String formattedTotalAmount; // VND formatted
    
    // Added item information
    public Long productId;
    public String productName;
    public int addedQuantity;
    public int newItemQuantity;
    public boolean itemAlreadyInCart;
    public String addedItemMessage; // e.g., "Đã thêm 2 sản phẩm" or "Đã cập nhật số lượng thành 5"
    
    // Product information
    public String formattedProductPrice;
    public int productStock;
    public String stockStatus; // "Còn X sản phẩm"
    
    // Error information (only if not success)
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor; // For UI styling
    
    // UI actions
    public String redirectUrl; // Where to redirect after adding
    public boolean showCartPopup; // Show cart summary popup
    
    // Constructor
    public AddToCartViewModel() {
        this.success = false;
        this.hasError = false;
        this.showCartPopup = false;
    }
}
