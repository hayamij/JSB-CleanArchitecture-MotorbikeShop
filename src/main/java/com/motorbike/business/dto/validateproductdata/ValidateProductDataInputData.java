package com.motorbike.business.dto.validateproductdata;

import java.math.BigDecimal;

public class ValidateProductDataInputData {
    private final String productName;
    private final String productCode;
    private final BigDecimal price;
    private final Integer stock;
    private final String category;

    public ValidateProductDataInputData(String productName, String productCode, BigDecimal price, Integer stock, String category) {
        this.productName = productName;
        this.productCode = productCode;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    // Constructor with primitive double for price (for backward compatibility)
    public ValidateProductDataInputData(String productName, String productCode, double price, int stock, String category) {
        this.productName = productName;
        this.productCode = productCode;
        this.price = BigDecimal.valueOf(price);
        this.stock = stock;
        this.category = category;
    }

    // Constructor with String for price (for backward compatibility)
    public ValidateProductDataInputData(String productName, String productCode, String price, double priceDouble, int stock) {
        this.productName = productName;
        this.productCode = productCode;
        this.price = BigDecimal.valueOf(priceDouble);
        this.stock = stock;
        this.category = null;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }
}
