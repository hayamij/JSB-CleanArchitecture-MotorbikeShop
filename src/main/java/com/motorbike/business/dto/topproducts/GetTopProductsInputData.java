package com.motorbike.business.dto.topproducts;

public class GetTopProductsInputData {
    private final int limit;
    
    public GetTopProductsInputData(int limit) {
        this.limit = limit;
    }
    
    public int getLimit() {
        return limit;
    }
}
