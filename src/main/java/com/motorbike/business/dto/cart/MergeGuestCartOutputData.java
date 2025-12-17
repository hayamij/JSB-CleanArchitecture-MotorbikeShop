package com.motorbike.business.dto.cart;

public class MergeGuestCartOutputData {
    private final boolean success;
    private final Long userCartId;
    private final boolean cartMerged;
    private final int mergedItemsCount;
    private final String errorCode;
    private final String errorMessage;
    
    private MergeGuestCartOutputData(boolean success, Long userCartId, boolean cartMerged,
                                    int mergedItemsCount, String errorCode, String errorMessage) {
        this.success = success;
        this.userCartId = userCartId;
        this.cartMerged = cartMerged;
        this.mergedItemsCount = mergedItemsCount;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static MergeGuestCartOutputData forSuccess(Long userCartId, boolean cartMerged, 
                                                     int mergedItemsCount) {
        return new MergeGuestCartOutputData(true, userCartId, cartMerged, 
                                           mergedItemsCount, null, null);
    }
    
    public static MergeGuestCartOutputData forError(String errorCode, String errorMessage) {
        return new MergeGuestCartOutputData(false, null, false, 0, errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public Long getUserCartId() {
        return userCartId;
    }
    
    public boolean isCartMerged() {
        return cartMerged;
    }
    
    public int getMergedItemsCount() {
        return mergedItemsCount;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
