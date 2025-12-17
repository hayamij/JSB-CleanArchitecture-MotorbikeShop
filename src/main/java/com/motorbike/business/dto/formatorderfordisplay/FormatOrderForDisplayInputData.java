package com.motorbike.business.dto.formatorderfordisplay;

import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.ChiTietDonHang;
import java.util.List;

public class FormatOrderForDisplayInputData {
    private final DonHang order;
    private final List<ChiTietDonHang> orderDetails;
    
    public FormatOrderForDisplayInputData(DonHang order) {
        this.order = order;
        this.orderDetails = null;
    }

    public FormatOrderForDisplayInputData(DonHang order, List<ChiTietDonHang> orderDetails) {
        this.order = order;
        this.orderDetails = orderDetails;
    }
    
    public DonHang getOrder() {
        return order;
    }

    public List<ChiTietDonHang> getOrderDetails() {
        return orderDetails;
    }
}
