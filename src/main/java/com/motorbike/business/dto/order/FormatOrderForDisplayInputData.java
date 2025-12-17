package com.motorbike.business.dto.order;

import com.motorbike.domain.entities.DonHang;

public class FormatOrderForDisplayInputData {
    private final DonHang donHang;
    
    public FormatOrderForDisplayInputData(DonHang donHang) {
        this.donHang = donHang;
    }
    
    public DonHang getDonHang() { return donHang; }
}
