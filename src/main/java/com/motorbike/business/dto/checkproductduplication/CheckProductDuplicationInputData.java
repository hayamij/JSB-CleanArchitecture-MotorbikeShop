package com.motorbike.business.dto.checkproductduplication;

public class CheckProductDuplicationInputData {
    private final String productName;
    private final String productCode;
    private final Long excludeProductId; // null nếu check cho create, có giá trị nếu check cho update

    public CheckProductDuplicationInputData(String productName, String productCode, Long excludeProductId) {
        this.productName = productName;
        this.productCode = productCode;
        this.excludeProductId = excludeProductId;
    }
    
    // Constructor with productCode and excludeProductId
    public CheckProductDuplicationInputData(String productCode, Long excludeProductId) {
        this.productName = null;
        this.productCode = productCode;
        this.excludeProductId = excludeProductId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public Long getExcludeProductId() {
        return excludeProductId;
    }
}
