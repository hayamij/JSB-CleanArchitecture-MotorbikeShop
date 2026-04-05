package com.motorbike.business.dto.user;

public class DeleteUserInputData {
    private final Long maTaiKhoan;
    
    public DeleteUserInputData(Long maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }
    
    public Long getMaTaiKhoan() { return maTaiKhoan; }
}
