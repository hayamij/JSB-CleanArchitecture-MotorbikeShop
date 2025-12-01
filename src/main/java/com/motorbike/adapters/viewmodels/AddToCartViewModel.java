package com.motorbike.adapters.viewmodels;

public class AddToCartViewModel {
    
    public boolean success;
    public String message;
    
    public Long cartId;
    public int totalItems;
    public int totalQuantity;
    public String formattedTotalAmount;
    
    public Long productId;
    public String productName;
    public int addedQuantity;
    public int newItemQuantity;
    public boolean itemAlreadyInCart;
    public String addedItemMessage;
    
    public String formattedProductPrice;
    public int productStock;
    public String stockStatus;
    
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor;
    
    public String redirectUrl;
    public boolean showCartPopup;
    
    public AddToCartViewModel() {
        this.success = false;
        this.hasError = false;
        this.showCartPopup = false;
    }
}
