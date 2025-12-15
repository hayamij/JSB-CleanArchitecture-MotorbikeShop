package com.motorbike.business.dto.accessory;

import java.math.BigDecimal;

/**
 * InputData cho use case Thêm phụ kiện.
 */
public class AddAccessoryInputData {

    // Thông tin chung của sản phẩm
    public final String name;          // tenSanPham
    public final String description;   // moTa
    public final BigDecimal price;     // gia
    public final String imageUrl;      // hinhAnh
    public final int stock;            // soLuongTonKho

    // Thông tin riêng của phụ kiện
    public final String type;          // loaiPhuKien
    public final String brand;         // thuongHieu
    public final String material;      // chatLieu
    public final String size;          // kichThuoc

    public final String productType;

    public AddAccessoryInputData(
            String name,
            String description,
            BigDecimal price,
            String imageUrl,
            int stock,
            String type,
            String brand,
            String material,
            String size,
            String productType
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.type = type;
        this.brand = brand;
        this.material = material;
        this.size = size;
        this.productType = productType;
    }
}
