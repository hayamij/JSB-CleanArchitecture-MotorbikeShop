package com.motorbike.business.dto.user;

import com.motorbike.domain.entities.VaiTro;

public class SearchUsersInputData {
    public String keyword;
    public VaiTro vaiTro;
    public Boolean hoatDong;

    public SearchUsersInputData(String keyword, VaiTro vaiTro, Boolean hoatDong) {
        this.keyword = keyword;
        this.vaiTro = vaiTro;
        this.hoatDong = hoatDong;
    }
}
