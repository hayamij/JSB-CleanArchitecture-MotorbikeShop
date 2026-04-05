package com.motorbike.business.dto.accessory;

public class DeleteAccessoryInputData {
    private final Long maSanPham;
    
    public DeleteAccessoryInputData(Long maSanPham) {
        this.maSanPham = maSanPham;
    }
    
    public Long getMaSanPham() { return maSanPham; }
}
