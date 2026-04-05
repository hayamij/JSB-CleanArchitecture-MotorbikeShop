package com.motorbike.business.dto.calculateproductprice;

import java.math.BigDecimal;

public class CalculateProductPriceInputData {
    private final BigDecimal basePrice;
    private final Integer discountPercent;

    public CalculateProductPriceInputData(BigDecimal basePrice, Integer discountPercent) {
        this.basePrice = basePrice;
        this.discountPercent = discountPercent;
    }
    
    // Constructor for tests accepting double and int
    public CalculateProductPriceInputData(double basePrice, int discountPercent) {
        this.basePrice = BigDecimal.valueOf(basePrice);
        this.discountPercent = discountPercent;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }
}
