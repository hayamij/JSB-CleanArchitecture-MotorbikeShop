package com.motorbike.business.dto.formatproductfordisplay;

import com.motorbike.domain.entities.SanPham;

public class FormatProductForDisplayInputData {
    private final SanPham product;

    public FormatProductForDisplayInputData(SanPham product) {
        this.product = product;
    }

    public SanPham getProduct() {
        return product;
    }
}
