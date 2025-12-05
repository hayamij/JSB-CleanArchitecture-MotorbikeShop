package com.motorbike.business.dto.motorbike;

public class DeleteMotorbikeInputData {
    private final Long maSanPham;
    
    public DeleteMotorbikeInputData(Long maSanPham) {
        this.maSanPham = maSanPham;
    }
    
    public Long getMaSanPham() {
        return maSanPham;
    }
}
