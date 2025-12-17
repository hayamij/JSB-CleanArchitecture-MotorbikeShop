package com.motorbike.business.dto.gettopproducts;

public class GetTopProductsInputData {
    private final int limit;
    
    public GetTopProductsInputData(int limit) {
        this.limit = limit;
    }
    
    public int getLimit() {
        return limit;
    }
}
