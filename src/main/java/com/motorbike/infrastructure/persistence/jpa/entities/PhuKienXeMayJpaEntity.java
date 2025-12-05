package com.motorbike.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "phu_kien_xe_may")
@PrimaryKeyJoinColumn(name = "ma_san_pham")
@DiscriminatorValue("PHU_KIEN")
public class PhuKienXeMayJpaEntity extends SanPhamJpaEntity {

    @Column(name = "loai_phu_kien", length = 100)
    private String loaiPhuKien;

    @Column(name = "thuong_hieu", length = 100)
    private String thuongHieu;

    @Column(name = "chat_lieu", length = 100)
    private String chatLieu;

    @Column(name = "kich_thuoc", length = 50)
    private String kichThuoc;

    public PhuKienXeMayJpaEntity() {
        super();
    }

    public PhuKienXeMayJpaEntity(String tenSanPham, String moTa, BigDecimal gia,
                                 String hinhAnh, int soLuongTonKho,
                                 String loaiPhuKien, String thuongHieu,
                                 String chatLieu, String kichThuoc) {
        super(tenSanPham, moTa, gia, hinhAnh, soLuongTonKho);
        this.loaiPhuKien = loaiPhuKien;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.kichThuoc = kichThuoc;
    }

    public String getLoaiPhuKien() {return loaiPhuKien;}

    public void setLoaiPhuKien(String loaiPhuKien) {this.loaiPhuKien = loaiPhuKien;}

    public String getThuongHieu() {return thuongHieu;}

    public void setThuongHieu(String thuongHieu) {this.thuongHieu = thuongHieu;}

    public String getChatLieu() {return chatLieu;}

    public void setChatLieu(String chatLieu) {this.chatLieu = chatLieu;}

    public String getKichThuoc() {return kichThuoc;}

    public void setKichThuoc(String kichThuoc) {this.kichThuoc = kichThuoc;}
}
