package com.motorbike.domain.entities;

public enum VaiTro {
    CUSTOMER("Khách hàng"),
    ADMIN("Quản trị viên");

    private final String moTa;
    VaiTro(String moTa) {this.moTa = moTa;}
    public String getMoTa() {return moTa;}
}
