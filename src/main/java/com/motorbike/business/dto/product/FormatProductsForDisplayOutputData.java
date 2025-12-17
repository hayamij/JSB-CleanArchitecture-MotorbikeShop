package com.motorbike.business.dto.product;

import com.motorbike.business.dto.product.GetAllProductsOutputData.ProductInfo;
import java.util.List;

public class FormatProductsForDisplayOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<ProductInfo> productInfos;

    private FormatProductsForDisplayOutputData(boolean success, String errorCode, String errorMessage, List<ProductInfo> productInfos) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.productInfos = productInfos;
    }

    public static FormatProductsForDisplayOutputData forSuccess(List<ProductInfo> productInfos) {
        return new FormatProductsForDisplayOutputData(true, null, null, productInfos);
    }

    public static FormatProductsForDisplayOutputData forError(String errorCode, String errorMessage) {
        return new FormatProductsForDisplayOutputData(false, errorCode, errorMessage, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<ProductInfo> getProductInfos() {
        return productInfos;
    }
}
