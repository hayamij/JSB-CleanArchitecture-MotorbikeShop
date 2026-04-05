package com.motorbike.business.dto.validatecart;

import java.util.ArrayList;
import java.util.List;

public class ValidateCartBeforeCheckoutOutputData {
    private final boolean success;
    private final boolean isValid;
    private final List<InvalidItemData> invalidItems;
    private final List<String> reasons;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ValidateCartBeforeCheckoutOutputData(boolean isValid, List<InvalidItemData> invalidItems, List<String> reasons) {
        this.success = true;
        this.isValid = isValid;
        this.invalidItems = invalidItems != null ? invalidItems : new ArrayList<>();
        this.reasons = reasons != null ? reasons : new ArrayList<>();
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    public ValidateCartBeforeCheckoutOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.isValid = false;
        this.invalidItems = new ArrayList<>();
        this.reasons = new ArrayList<>();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isValid() {
        return isValid;
    }

    public List<InvalidItemData> getInvalidItems() {
        return invalidItems;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static ValidateCartBeforeCheckoutOutputData forError(String errorCode, String errorMessage) {
        return new ValidateCartBeforeCheckoutOutputData(errorCode, errorMessage);
    }

    public static class InvalidItemData {
        private final Long productId;
        private final String productName;
        private final int requestedQuantity;
        private final int availableStock;
        private final String reason;

        public InvalidItemData(Long productId, String productName, int requestedQuantity, 
                             int availableStock, String reason) {
            this.productId = productId;
            this.productName = productName;
            this.requestedQuantity = requestedQuantity;
            this.availableStock = availableStock;
            this.reason = reason;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public int getRequestedQuantity() {
            return requestedQuantity;
        }

        public int getAvailableStock() {
            return availableStock;
        }

        public String getReason() {
            return reason;
        }
    }
}
