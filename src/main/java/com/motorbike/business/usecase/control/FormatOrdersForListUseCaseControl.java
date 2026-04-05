package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatordersforlist.FormatOrdersForListInputData;
import com.motorbike.business.dto.formatordersforlist.FormatOrdersForListOutputData;
import com.motorbike.business.dto.formatordersforlist.FormatOrdersForListOutputData.OrderListItem;
import com.motorbike.business.usecase.input.FormatOrdersForListInputBoundary;
import com.motorbike.business.usecase.output.FormatOrdersForListOutputBoundary;
import com.motorbike.domain.entities.DonHang;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UC-81: Format Orders For List Use Case
 * Responsibility: Format DonHang entities into OrderListItem DTOs
 * - Maps all necessary fields for list display
 * - Calculates product count and total quantity
 * - Formats status and payment method
 */
public class FormatOrdersForListUseCaseControl implements FormatOrdersForListInputBoundary {
    
    private final FormatOrdersForListOutputBoundary outputBoundary;
    
    public FormatOrdersForListUseCaseControl(FormatOrdersForListOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public FormatOrdersForListOutputData execute(FormatOrdersForListInputData inputData) {
        if (outputBoundary != null) {
            FormatOrdersForListOutputData outputData = formatInternal(inputData);
            outputBoundary.present(outputData);
            return outputData;
        } else {
            return formatInternal(inputData);
        }
    }
    
    /**
     * Internal method for direct delegation from other usecases
     * Returns output data without going through presenter
     */
    public FormatOrdersForListOutputData formatInternal(FormatOrdersForListInputData inputData) {
        try {
            if (inputData == null || inputData.getOrders() == null) {
                return FormatOrdersForListOutputData.forError("INVALID_INPUT", "Orders list cannot be null");
            }
            
            List<OrderListItem> orderItems = inputData.getOrders().stream()
                    .map(this::mapToOrderListItem)
                    .collect(Collectors.toList());
            
            return FormatOrdersForListOutputData.forSuccess(orderItems);
            
        } catch (Exception e) {
            return FormatOrdersForListOutputData.forError("FORMAT_ERROR", e.getMessage());
        }
    }
    
    private OrderListItem mapToOrderListItem(DonHang donHang) {
        // Calculate product count
        int productCount = donHang.getDanhSachSanPham() != null 
                ? donHang.getDanhSachSanPham().size() 
                : 0;
        
        // Calculate total quantity
        int totalQuantity = donHang.getDanhSachSanPham() != null
                ? donHang.getDanhSachSanPham().stream()
                        .mapToInt(item -> item.getSoLuong())
                        .sum()
                : 0;
        
        return new OrderListItem(
                donHang.getMaDonHang(),
                String.valueOf(donHang.getMaTaiKhoan()),
                donHang.getTenNguoiNhan(),
                donHang.getSoDienThoai(),
                donHang.getDiaChiGiaoHang(),
                donHang.getTrangThai().name(),
                donHang.getTongTien(),
                productCount,
                totalQuantity,
                donHang.getNgayDat(),
                donHang.getGhiChu(),
                donHang.getPhuongThucThanhToan().name()
        );
    }
}
