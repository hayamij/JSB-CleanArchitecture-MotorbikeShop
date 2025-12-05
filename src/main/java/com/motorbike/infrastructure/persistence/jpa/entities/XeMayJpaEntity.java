package com.motorbike.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "xe_may")
@PrimaryKeyJoinColumn(name = "ma_san_pham")
@DiscriminatorValue("XE_MAY")
public class XeMayJpaEntity extends SanPhamJpaEntity {

    @Column(name = "hang_xe", length = 100)
    private String hangXe;

    @Column(name = "dong_xe", length = 100)
    private String dongXe;

    @Column(name = "mau_sac", length = 50)
    private String mauSac;

    @Column(name = "nam_san_xuat")
    private int namSanXuat;

    @Column(name = "dung_tich")
    private int dungTich;

    public XeMayJpaEntity() {
        super();
    }

    public XeMayJpaEntity(String tenSanPham, String moTa, BigDecimal gia,
                          String hinhAnh, int soLuongTonKho,
                          String hangXe, String dongXe, String mauSac,
                          int namSanXuat, int dungTich) {
        super(tenSanPham, moTa, gia, hinhAnh, soLuongTonKho);
        this.hangXe = hangXe;
        this.dongXe = dongXe;
        this.mauSac = mauSac;
        this.namSanXuat = namSanXuat;
        this.dungTich = dungTich;
    }

    public String getHangXe() {return hangXe;}

    public void setHangXe(String hangXe) {this.hangXe = hangXe;}

    public String getDongXe() {return dongXe;}

    public void setDongXe(String dongXe) {this.dongXe = dongXe;}

    public String getMauSac() {return mauSac;}

    public void setMauSac(String mauSac) {this.mauSac = mauSac;}

    public int getNamSanXuat() {return namSanXuat;}

    public void setNamSanXuat(int namSanXuat) {this.namSanXuat = namSanXuat;}

    public int getDungTich() {return dungTich;}

    public void setDungTich(int dungTich) {this.dungTich = dungTich;}
}
