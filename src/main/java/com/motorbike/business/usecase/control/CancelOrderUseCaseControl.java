package com.motorbike.business.usecase.control;

import java.math.BigDecimal;

import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.exceptions.CannotCancelOrderException;

/**
 * Cancel Order Use Case Control
 * Business Rules:
 * - Chỉ có thể hủy đơn hàng với status = CHO_XAC_NHAN
 * - Không thể hủy đơn đã xác nhận thanh toán
 * - Phải recover tồn kho của tất cả sản phẩm
 * - Lưu lý do hủy để thống kê
 */
public class CancelOrderUseCaseControl 
        extends AbstractUseCaseControl<CancelOrderInputData, CancelOrderOutputBoundary> {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    public CancelOrderUseCaseControl(
            CancelOrderOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        super(outputBoundary);
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    protected void executeBusinessLogic(CancelOrderInputData inputData) throws Exception {
        try {
            // 1️⃣ Lấy đơn hàng
            DonHang donHang = orderRepository.findById(inputData.getOrderId())
                .orElseThrow(() -> new CannotCancelOrderException(
                    "ORDER_NOT_FOUND",
                    "Không tìm thấy đơn hàng: " + inputData.getOrderId()
                ));
            
            // 2️⃣ Kiểm tra quyền (user phải là chủ đơn)
            if (!donHang.getMaTaiKhoan().equals(inputData.getUserId())) {
                throw new CannotCancelOrderException(
                    "PERMISSION_DENIED",
                    "Bạn không có quyền hủy đơn hàng này"
                );
            }
            
            // 3️⃣ Kiểm tra trạng thái (chỉ hủy được CHO_XAC_NHAN)
            if (donHang.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN) {
                throw new CannotCancelOrderException(
                    "INVALID_ORDER_STATUS",
                    "Chỉ có thể hủy đơn hàng ở trạng thái 'Chờ xác nhận'. " +
                    "Trạng thái hiện tại: " + donHang.getTrangThai().getMoTa()
                );
            }
            
            // 4️⃣ 🔄 RECOVER TỒN KHO (QUAN TRỌNG!)
            BigDecimal totalRefund = BigDecimal.ZERO;
            for (ChiTietDonHang chiTiet : donHang.getDanhSachSanPham()) {
                SanPham sanPham = productRepository.findById(chiTiet.getMaSanPham())
                    .orElseThrow(() -> new CannotCancelOrderException(
                        "PRODUCT_NOT_FOUND",
                        "Sản phẩm không tồn tại: " + chiTiet.getMaSanPham()
                    ));
                
                // Cộng lại tồn kho
                sanPham.tangTonKho(chiTiet.getSoLuong());
                productRepository.save(sanPham);
                
                // Tính tổng hoàn tiền
                totalRefund = totalRefund.add(chiTiet.getThanhTien());
            }
            
            // 5️⃣ CẬP NHẬT TRẠNG THÁI ĐƠNHÀNG
            donHang.huyDonHang();  // Entity tự xử lý logic hủy
            DonHang cancelledOrder = orderRepository.save(donHang);
            
            // 6️⃣ OUTPUT
            CancelOrderOutputData outputData = CancelOrderOutputData.forSuccess(
                cancelledOrder.getMaDonHang(),
                cancelledOrder.getMaTaiKhoan(),
                cancelledOrder.getTrangThai().name(),
                totalRefund,
                inputData.getCancelReason()
            );
            
            outputBoundary.present(outputData);
            
        } catch (CannotCancelOrderException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(CancelOrderInputData inputData) {
        checkInputNotNull(inputData);
        
        if (inputData.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID không được null");
        }
        
        if (inputData.getUserId() == null) {
            throw new IllegalArgumentException("User ID không được null");
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        CancelOrderOutputData outputData = CancelOrderOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode = "SYSTEM_ERROR";
        String message = "Đã xảy ra lỗi: " + e.getMessage();
        
        try {
            throw e;
        } catch (CannotCancelOrderException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (Exception ex) {
            // Keep default
        }
        
        CancelOrderOutputData outputData = CancelOrderOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}