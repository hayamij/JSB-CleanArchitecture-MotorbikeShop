package com.motorbike.business.dto.user;

import com.motorbike.domain.entities.TaiKhoan;
import java.util.List;

/**
 * UC-54: Format User For Display
 * Input data with user entities to be formatted
 */
public class FormatUserForDisplayInputData {
    
    private final List<TaiKhoan> users;
    
    public FormatUserForDisplayInputData(List<TaiKhoan> users) {
        this.users = users;
    }
    
    public List<TaiKhoan> getUsers() {
        return users;
    }
}
