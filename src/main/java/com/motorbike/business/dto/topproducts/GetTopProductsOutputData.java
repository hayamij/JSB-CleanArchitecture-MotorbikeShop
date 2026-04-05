package com.motorbike.business.dto.topproducts;

import java.util.List;

public class GetTopProductsOutputData {
    private final boolean success;
    private final List<ProductSalesInfo> products;
    private final String errorCode;
    private final String errorMessage;
    
    private GetTopProductsOutputData(boolean success, List<ProductSalesInfo> products, 
                                     String errorCode, String errorMessage) {
        this.success = success;
        this.products = products;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static GetTopProductsOutputData forSuccess(List<ProductSalesInfo> products) {
        return new GetTopProductsOutputData(true, products, null, null);
    }
    
    public static GetTopProductsOutputData forError(String errorCode, String errorMessage) {
        return new GetTopProductsOutputData(false, null, errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public List<ProductSalesInfo> getProducts() {
        return products;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public static class ProductSalesInfo {
        private final Long productId;
        private final String productName;
        private final int totalSold;
        
        public ProductSalesInfo(Long productId, String productName, int totalSold) {
            this.productId = productId;
            this.productName = productName;
            this.totalSold = totalSold;
        }
        
        public Long getProductId() {
            return productId;
        }
        
        public String getProductName() {
            return productName;
        }
        
        public int getTotalSold() {
            return totalSold;
        }
    }
}
