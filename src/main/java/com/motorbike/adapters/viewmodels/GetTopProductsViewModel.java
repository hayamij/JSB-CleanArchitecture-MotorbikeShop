package com.motorbike.adapters.viewmodels;

import java.util.List;

public class GetTopProductsViewModel {
    public boolean success;
    public List<ProductSalesDisplay> products;
    public String errorCode;
    public String errorMessage;
    
    public static class ProductSalesDisplay {
        public Long productId;
        public String productName;
        public int totalSold;
    }
}
