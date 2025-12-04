package com.motorbike.business.dto.motorbike;

import java.math.BigDecimal;

/**
 * InputData cho use case Thêm xe máy.
 *
 * Dữ liệu này đến từ Controller (request từ UI/API),
 * rồi được truyền vào lớp UseCaseControl.
 */
public class AddMotorbikeInputData {

    // Thông tin chung của sản phẩm
    public final String name;          // tenSanPham
    public final String description;   // moTa
    public final BigDecimal price;     // gia
    public final String imageUrl;      // hinhAnh
    public final int stock;            // soLuongTonKho cần cộng thêm (B)

    // Thông tin riêng của xe máy
    public final String brand;         // hangXe
    public final String model;         // dongXe
    public final String color;         // mauSac
    public final int year;             // namSanXuat
    public final int displacement;     // dungTich (cc)

    public final String productType;


    public AddMotorbikeInputData(
            String name,
            String description,
            BigDecimal price,
            String imageUrl,
            int stock,
            String brand,
            String model,
            String color,
            int year,
            int displacement,
            String productType
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.displacement = displacement;
        this.productType = productType;
    }
}
