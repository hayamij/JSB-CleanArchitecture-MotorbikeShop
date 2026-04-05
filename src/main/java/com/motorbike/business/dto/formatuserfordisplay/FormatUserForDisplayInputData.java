package com.motorbike.business.dto.formatuserfordisplay;

import com.motorbike.domain.entities.TaiKhoan;

public class FormatUserForDisplayInputData {
    private final TaiKhoan user;

    public FormatUserForDisplayInputData(TaiKhoan user) {
        this.user = user;
    }

    public TaiKhoan getUser() {
        return user;
    }
}
