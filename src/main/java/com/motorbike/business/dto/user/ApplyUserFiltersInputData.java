package com.motorbike.business.dto.user;

import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import java.util.List;

/**
 * UC-73: Apply User Filters
 * Input data for filtering user list by keyword, role, and active status
 */
public class ApplyUserFiltersInputData {
    
    private final List<TaiKhoan> users;
    private final String keyword;
    private final VaiTro vaiTro;
    private final Boolean hoatDong;
    
    public ApplyUserFiltersInputData(List<TaiKhoan> users, String keyword, VaiTro vaiTro, Boolean hoatDong) {
        this.users = users;
        this.keyword = keyword;
        this.vaiTro = vaiTro;
        this.hoatDong = hoatDong;
    }
    
    public List<TaiKhoan> getUsers() {
        return users;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public VaiTro getVaiTro() {
        return vaiTro;
    }
    
    public Boolean getHoatDong() {
        return hoatDong;
    }
}
