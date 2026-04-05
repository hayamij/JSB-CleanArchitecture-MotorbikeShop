package com.motorbike.business.dto.product;

import com.motorbike.domain.entities.SanPham;
import java.util.List;

public class FormatProductsForDisplayInputData {
    private final List<SanPham> products;

    public FormatProductsForDisplayInputData(List<SanPham> products) {
        this.products = products;
    }

    public List<SanPham> getProducts() {
        return products;
    }
}
