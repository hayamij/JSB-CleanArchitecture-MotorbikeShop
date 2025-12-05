package com.motorbike.business.dto.motorbike;

import java.math.BigDecimal;

<<<<<<< HEAD
public class AddMotorbikeInputData {
    private final String tenSanPham;
    private final String moTa;
    private final BigDecimal gia;
    private final String hinhAnh;
    private final int soLuongTonKho;
    private final String hangXe;
    private final String dongXe;
    private final String mauSac;
    private final int namSanXuat;
    private final int dungTich;
    
    public AddMotorbikeInputData(String tenSanPham, String moTa, BigDecimal gia,
                                String hinhAnh, int soLuongTonKho, String hangXe,
                                String dongXe, String mauSac, int namSanXuat, int dungTich) {
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.soLuongTonKho = soLuongTonKho;
        this.hangXe = hangXe;
        this.dongXe = dongXe;
        this.mauSac = mauSac;
        this.namSanXuat = namSanXuat;
        this.dungTich = dungTich;
    }
    
    public String getTenSanPham() { return tenSanPham; }
    public String getMoTa() { return moTa; }
    public BigDecimal getGia() { return gia; }
    public String getHinhAnh() { return hinhAnh; }
    public int getSoLuongTonKho() { return soLuongTonKho; }
    public String getHangXe() { return hangXe; }
    public String getDongXe() { return dongXe; }
    public String getMauSac() { return mauSac; }
    public int getNamSanXuat() { return namSanXuat; }
    public int getDungTich() { return dungTich; }
=======
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
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
}
