package com.motorbike.domain.entities;

public enum VaiTro {
    CUSTOMER("Khách hàng"),
    ADMIN("Quản trị viên");

    private final String moTa;
    VaiTro(String moTa) {this.moTa = moTa;}
    public String getMoTa() {return moTa;}
    
    public static VaiTro fromString(String value) {
        if (value == null) return CUSTOMER;
        try {
            return VaiTro.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CUSTOMER;
        }
    }
}
