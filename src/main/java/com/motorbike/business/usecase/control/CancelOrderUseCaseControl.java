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
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class CancelOrderUseCaseControl {
    
    private final CancelOrderOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    public CancelOrderUseCaseControl(
            CancelOrderOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    
    public void execute(CancelOrderInputData inputData) {
        CancelOrderOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            com.motorbike.domain.entities.TaiKhoan.checkInput(inputData.getUserId());
            
            if (inputData.getOrderId() == null) {
                throw ValidationException.nullOrderId();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        DonHang donHang = null;
        if (errorException == null) {
            try {
                donHang = orderRepository.findById(inputData.getOrderId())
                    .orElseThrow(() -> DomainException.cannotCancelOrder(
                        "Không tìm thấy đơn hàng: " + inputData.getOrderId()
                    ));
                
                if (!donHang.getMaTaiKhoan().equals(inputData.getUserId())) {
                    throw DomainException.cannotCancelOrder(
                        "Bạn không có quyền hủy đơn hàng này"
                    );
                }
                
                if (donHang.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN) {
                    throw DomainException.cannotCancelOrder(
                        "Chỉ có thể hủy đơn hàng ở trạng thái 'Chờ xác nhận'. " +
                        "Trạng thái hiện tại: " + donHang.getTrangThai().getMoTa()
                    );
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && donHang != null) {
            try {
                BigDecimal totalRefund = BigDecimal.ZERO;
                for (ChiTietDonHang chiTiet : donHang.getDanhSachSanPham()) {
                    SanPham sanPham = productRepository.findById(chiTiet.getMaSanPham())
                        .orElseThrow(() -> DomainException.productNotFound(
                            "Sản phẩm không tồn tại: " + chiTiet.getMaSanPham()
                        ));
                    
                    sanPham.tangTonKho(chiTiet.getSoLuong());
                    productRepository.save(sanPham);
                    
                    totalRefund = totalRefund.add(chiTiet.getThanhTien());
                }
                
                donHang.huyDonHang();
                DonHang cancelledOrder = orderRepository.save(donHang);
                
                outputData = CancelOrderOutputData.forSuccess(
                    cancelledOrder.getMaDonHang(),
                    cancelledOrder.getMaTaiKhoan(),
                    cancelledOrder.getTrangThai().name(),
                    totalRefund,
                    inputData.getCancelReason()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            } else if (errorException instanceof com.motorbike.domain.exceptions.SystemException) {
                errorCode = ((com.motorbike.domain.exceptions.SystemException) errorException).getErrorCode();
            }
            
            outputData = CancelOrderOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
